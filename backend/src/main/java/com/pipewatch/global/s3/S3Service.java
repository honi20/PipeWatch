package com.pipewatch.global.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;

import com.pipewatch.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.pipewatch.global.statusCode.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${S3_URL}")
	private String bucketUrl;

	public String upload(MultipartFile multipartFile, String modelUuid) throws IOException {
		if (multipartFile.isEmpty() || Objects.isNull(multipartFile.getOriginalFilename())) {
			throw new BaseException(FILE_UPLOAD_FAIL);
		}

		return bucketUrl + uploadS3(multipartFile, modelUuid);
	}

	private String uploadS3(MultipartFile multipartFile, String fileName) throws IOException {
		validateImageFileExtention(multipartFile.getOriginalFilename());

		String originalFilename = multipartFile.getOriginalFilename(); //원본 파일 명
		String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); //확장자 명

		String dirName, contentType;

		// modeling file
		if (extension.equals(".gltf")) {
			dirName = "models";
			contentType = "model/gltf+json";
		}
		// thumbnail file
		else {
			dirName = "thumbnails";
			contentType = "image/**";
		}

		String s3FileName = dirName + "/" + fileName + extension;
		InputStream is = multipartFile.getInputStream();
		byte[] bytes = IOUtils.toByteArray(is);

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(contentType);
		objectMetadata.setContentLength(bytes.length);

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

		try {
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, s3FileName, byteArrayInputStream, objectMetadata);
			amazonS3Client.putObject(putObjectRequest);
		} catch (Exception e) {
			throw new BaseException(FILE_UPLOAD_FAIL);
		} finally {
			byteArrayInputStream.close();
			is.close();
		}

		return s3FileName;
	}

	public InputStream download(String fileName) throws IOException {
		S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, fileName));

		return s3Object.getObjectContent();
	}

	public void fileDelete(String fileUrl, String dirName) throws BaseException {
		String originalFilename = fileUrl.substring(fileUrl.indexOf(dirName));

		try {
			try {
				amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, originalFilename));
			} catch (AmazonServiceException e) {
				System.err.println(e.getErrorMessage());
				throw new BaseException(FILE_DELETE_FAIL);
			}
		} catch (Exception exception) {
			throw new BaseException(FILE_DELETE_FAIL);
		}
	}

	private void validateImageFileExtention(String filename) {
		int lastDotIndex = filename.lastIndexOf(".");
		if (lastDotIndex == -1) {
			throw new BaseException(FILE_UPLOAD_FAIL);
		}

		String extention = filename.substring(lastDotIndex + 1).toLowerCase();
		List<String> allowedExtentionList = Arrays.asList("gltf", "png", "jpg", "jpeg");

		if (!allowedExtentionList.contains(extention)) {
			throw new BaseException(INVALID_FILE_EXTENSION);
		}
	}
}