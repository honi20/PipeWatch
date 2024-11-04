package com.pipewatch.domain.pipelineModel.service;

import com.pipewatch.domain.pipeline.model.entity.Pipe;
import com.pipewatch.domain.pipeline.model.entity.Pipeline;
import com.pipewatch.domain.pipeline.repository.PipeRepository;
import com.pipewatch.domain.pipeline.repository.PipelineRepository;
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
import org.json.simple.JSONArray;
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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.pipewatch.global.statusCode.ErrorCode.FORBIDDEN_USER_ROLE;
import static com.pipewatch.global.statusCode.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PipelineModelServiceImpl implements PipelineModelService {
	private final UserRepository userRepository;
	private final PipelineModelRepository pipelineModelRepository;
	private final PipelineRepository pipelineRepository;
	private final PipeRepository pipeRepository;
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
	public PipelineModelResponse.CreateModelingDto createModeling(PipelineModelRequest.ModelingDto requestDto) throws IOException, ParseException {
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
		JSONArray nodes = (JSONArray) jsonObject.get("nodes");
		Map<String, Pipeline> pipelineMap = new HashMap<>();

		for (int i = 0; i < nodes.size(); i++) {
			JSONObject node = (JSONObject) nodes.get(i);
			String nodeName = (String) node.get("name");

			if (nodeName.startsWith("PipeObj_") && !nodeName.contains("Segment")) {
				String[] parts = nodeName.split("_");
				String pipelineNumber = parts[1];

				Pipeline pipeline = Pipeline.builder()
						.name(nodeName)
						.uuid(nodeName + "_" + UUID)
						.pipelineModel(pipelineModel)
						.build();

				pipelineRepository.save(pipeline);
				pipelineMap.put(pipelineNumber, pipeline);
			} else if (nodeName.startsWith("PipeObj_") && nodeName.contains("Segment")) {
				String[] parts = nodeName.split("_");
				String pipelineNumber = parts[1];
				Pipeline relatedPipeline = pipelineMap.get(pipelineNumber);

				if (relatedPipeline != null) {
					Pipe pipe = Pipe.builder()
							.name(nodeName)
							.uuid(nodeName + "_" + UUID)
							.pipeline(relatedPipeline)
							.build();

					pipeRepository.save(pipe);
				}
			}
		}

		return PipelineModelResponse.CreateModelingDto.builder()
				.modelId(pipelineModel.getId())
				.build();
	}

	private JSONObject getJsonObject(String modelUrl) throws IOException, ParseException {
		String fileName = modelUrl.split(S3_URL)[1];
		InputStream inputStream = s3Service.download(fileName);
		JSONParser jsonParser = new JSONParser();

		return (JSONObject) jsonParser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	}
}