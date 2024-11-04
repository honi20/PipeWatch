package com.pipewatch.domain.pipelineModel.service;

import com.pipewatch.domain.pipelineModel.model.dto.PipelineModelRequest;
import com.pipewatch.domain.pipelineModel.model.dto.PipelineModelResponse;
import com.pipewatch.domain.pipelineModel.model.entity.PipelineModel;
import com.pipewatch.domain.pipelineModel.repository.PipelineModelRepository;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import com.pipewatch.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.pipewatch.global.statusCode.ErrorCode.FORBIDDEN_USER_ROLE;
import static com.pipewatch.global.statusCode.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PipelineModelServiceImpl implements PipelineModelService{
	private final UserRepository userRepository;
	private final PipelineModelRepository pipelineModelRepository;
	private final S3Service s3Service;

	@Value("${S3_URL}")
	private String S3_URL;

	@Override
	@Transactional
	public PipelineModelResponse.FileUploadDto uploadFile(Long userId, MultipartFile file) throws IOException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 관리자 및 기업만 가능
		if (user.getRole() == Role.USER || user.getRole() == Role.EMPLOYEE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		String UUID = java.util.UUID.randomUUID().toString();

		PipelineModel pipelineModel = PipelineModel.builder()
				.user(user)
				.isCompleted(true)
				.uuid(UUID)
				.build();

		if (!file.isEmpty()) {
			String imgUrl = s3Service.upload(file, "pipeline/model", "pipeline_" + UUID);
			pipelineModel.updateModelingUrl(imgUrl);
		}

		pipelineModelRepository.save(pipelineModel);

		return PipelineModelResponse.FileUploadDto.builder()
				.modelId(pipelineModel.getId())
				.build();
	}

	@Override
	public PipelineModelResponse.FileUploadDto createModeling(PipelineModelRequest.ModelingDto requestDto) throws IOException, ParseException {
		User user = userRepository.findByUuid(requestDto.getUserUuid());
		if (user == null) {
			throw new BaseException(USER_NOT_FOUND);
		}

		// 일반 사원이나 직원은 생성 불가
		if (user.getRole() == Role.USER || user.getRole() == Role.EMPLOYEE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		String UUID = java.util.UUID.randomUUID().toString();

		PipelineModel pipelineModel = PipelineModel.builder()
				.user(user)
				.modelingUrl(requestDto.getModelUrl())
				.previewImgUrl(requestDto.getPreviewImgUrl())
				.isCompleted(true)
				.uuid(UUID)
				.build();

		pipelineModelRepository.save(pipelineModel);

		// Json 정보 추출
		JSONObject jsonObject = getJsonObject(requestDto.getModelUrl());


		// TODO: Object 기준으로 데이터 뽑아서 Pipeline, Pipe 테이블에 저장


		return null;
	}

	private JSONObject getJsonObject(String modelUrl) throws IOException, ParseException {
		String fileName = modelUrl.split(S3_URL)[1];
		InputStream inputStream = s3Service.download(fileName);
		JSONParser jsonParser = new JSONParser();

		return (JSONObject)jsonParser.parse(new InputStreamReader(inputStream, "UTF-8"));
	}
}