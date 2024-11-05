package com.pipewatch.domain.pipelineModel.service;

import com.pipewatch.domain.enterprise.model.entity.BuildingAndFloor;
import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import com.pipewatch.domain.enterprise.repository.BuildingRepository;
import com.pipewatch.domain.enterprise.repository.EnterpriseRepository;
import com.pipewatch.domain.pipeline.model.entity.Pipe;
import com.pipewatch.domain.pipeline.model.entity.Pipeline;
import com.pipewatch.domain.pipeline.repository.PipeRepository;
import com.pipewatch.domain.pipeline.repository.PipelineRepository;
import com.pipewatch.domain.pipelineModel.model.dto.PipelineModelRequest;
import com.pipewatch.domain.pipelineModel.model.dto.PipelineModelResponse;
import com.pipewatch.domain.pipelineModel.model.entity.PipelineModel;
import com.pipewatch.domain.pipelineModel.model.entity.PipelineModelMemo;
import com.pipewatch.domain.pipelineModel.repository.PipelineModelCustomRepository;
import com.pipewatch.domain.pipelineModel.repository.PipelineModelMemoRepository;
import com.pipewatch.domain.pipelineModel.repository.PipelineModelRepository;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.EmployeeRepository;
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
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pipewatch.global.statusCode.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PipelineModelServiceImpl implements PipelineModelService {
	private final UserRepository userRepository;
	private final PipelineModelRepository pipelineModelRepository;
	private final PipelineModelMemoRepository pipelineModelMemoRepository;
	private final PipelineRepository pipelineRepository;
	private final PipeRepository pipeRepository;
	private final S3Service s3Service;
	private final BuildingRepository buildingRepository;
	private final EnterpriseRepository enterpriseRepository;
	private final EmployeeRepository employeeRepository;
	private final PipelineModelCustomRepository pipelineModelCustomRepository;

	@Value("${S3_URL}")
	private String S3_URL;

	@Override
	@Transactional
	public PipelineModelResponse.FileUploadDto uploadFile(Long userId, MultipartFile file) throws IOException, ParseException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 관리자 및 기업만 가능
		if (user.getRole() == Role.USER || user.getRole() == Role.EMPLOYEE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		Enterprise enterprise = getEnterprise(user);

		String UUID = java.util.UUID.randomUUID().toString();

		PipelineModel pipelineModel = PipelineModel.builder()
				.user(user)
				.enterprise(enterprise)
				.isCompleted(true)
				.uuid(UUID)
				.build();

		String imgUrl = null;
		if (!file.isEmpty()) {
			imgUrl = s3Service.upload(file, "models", "PipeLine_" + UUID);
			pipelineModel.updateModelingUrl(imgUrl);
		}

		// TODO: Fast API로 썸네일 이미지 url 요청보내기

		pipelineModelRepository.save(pipelineModel);

		if (imgUrl != null) {
			savePipelineObject(imgUrl, UUID, pipelineModel);
		}

		return PipelineModelResponse.FileUploadDto.builder()
				.modelId(pipelineModel.getId())
				.build();
	}

	@Override
	@Transactional
	public PipelineModelResponse.CreateModelingDto createModeling(PipelineModelRequest.ModelingDto requestDto) throws IOException, ParseException {
		User user = userRepository.findByUuid(requestDto.getUserUuid());
		if (user == null) {
			throw new BaseException(USER_NOT_FOUND);
		}

		// 일반 사원이나 직원은 생성 불가
		if (user.getRole() == Role.USER || user.getRole() == Role.EMPLOYEE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		Enterprise enterprise = getEnterprise(user);

		String UUID = java.util.UUID.randomUUID().toString();

		PipelineModel pipelineModel = PipelineModel.builder()
				.user(user)
				.enterprise(enterprise)
				.modelingUrl(requestDto.getModelUrl())
				.previewImgUrl(requestDto.getPreviewImgUrl())
				.isCompleted(true)
				.uuid(UUID)
				.build();

		pipelineModelRepository.save(pipelineModel);

		savePipelineObject(requestDto.getModelUrl(), UUID, pipelineModel);

		return PipelineModelResponse.CreateModelingDto.builder()
				.modelId(pipelineModel.getId())
				.build();
	}

	@Override
	@Transactional
	public void initModel(Long userId, Long modelId, PipelineModelRequest.InitDto requestDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		PipelineModel pipelineModel = pipelineModelRepository.findById(modelId)
				.orElseThrow(() -> new BaseException(PIPELINE_MODEL_NOT_FOUND));

		// 기업이나 관리자 유저만 가능
		if (user.getRole() == Role.USER || user.getRole() == Role.EMPLOYEE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		// 건물명이 없는 경우, 건물 테이블에 추가
		BuildingAndFloor buildingAndFloor = buildingRepository.findByNameAndFloor(requestDto.getBuilding(), requestDto.getFloor());
		if (buildingAndFloor == null) {
			Enterprise enterprise = getEnterprise(user);

			if (enterprise != null) {
				buildingAndFloor = BuildingAndFloor.builder()
						.name(requestDto.getBuilding())
						.floor(requestDto.getFloor())
						.enterprise(enterprise)
						.build();

				buildingRepository.save(buildingAndFloor);
			}
		}
		System.out.println(buildingAndFloor.getId() + "***");

		// 파이프라인 모델명 설정
		pipelineModel.updateName(requestDto.getName());
		pipelineModel.updateBuilding(buildingAndFloor);
		pipelineModelRepository.save(pipelineModel);
	}

	@Override
	public PipelineModelResponse.ListDto getModelList(Long userId, String building, Integer floor) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		if (user.getRole() == Role.USER) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		// 기업 정보 가져오기
		Enterprise enterprise = getEnterprise(user);

		List<PipelineModel> modelList = pipelineModelCustomRepository.findAllByBuildingAndFloor(enterprise, building, floor);

		List<PipelineModelResponse.PipelineModelDto> modelDtos = modelList.stream()
				.map(PipelineModelResponse.PipelineModelDto::toDto)
				.collect(Collectors.toList());

		return PipelineModelResponse.ListDto.builder()
				.models(modelDtos)
				.build();
	}

	@Override
	public PipelineModelResponse.DetailDto getModelDetail(Long userId, Long modelId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		if (user.getRole() == Role.USER) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		// Model
		PipelineModel model = pipelineModelRepository.findById(modelId)
				.orElseThrow(() -> new BaseException(PIPELINE_MODEL_NOT_FOUND));

		// Pipelines Uuid
		List<Pipe> pipes = pipelineModelCustomRepository.findPipeByModel(modelId);
		List<PipelineModelResponse.PipelineDto> pipelines = getPipelineDto(pipes);

		return PipelineModelResponse.DetailDto.toDto(model, pipelines);
	}

	@Override
	@Transactional
	public void modifyModel(Long userId, Long modelId, PipelineModelRequest.ModifyDto requestDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		PipelineModel pipelineModel = pipelineModelRepository.findById(modelId)
				.orElseThrow(() -> new BaseException(PIPELINE_MODEL_NOT_FOUND));

		// 기업이나 관리자 유저만 가능
		if (user.getRole() == Role.USER || user.getRole() == Role.EMPLOYEE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		pipelineModel.updateName(requestDto.getName());
		pipelineModelRepository.save(pipelineModel);
	}

	@Override
	@Transactional
	public void deleteModel(Long userId, Long modelId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		PipelineModel pipelineModel = pipelineModelRepository.findById(modelId)
				.orElseThrow(() -> new BaseException(PIPELINE_MODEL_NOT_FOUND));

		// 기업이나 관리자 유저만 가능
		if (user.getRole() == Role.USER || user.getRole() == Role.EMPLOYEE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		// pipeline 삭제
		List<Pipeline> pipelines = pipelineRepository.findByPipelineModelId(modelId);
		pipelineRepository.deleteAll(pipelines);
		// s3 데이터 삭제
		s3Service.fileDelete(pipelineModel.getModelingUrl(), "models/");
		// Model 삭제
		pipelineModelRepository.delete(pipelineModel);
	}

	private List<PipelineModelResponse.PipelineDto> getPipelineDto(List<Pipe> pipes) {
		// pipeline의 uuid를 기준으로 group화한 후 각 PipelineDto로 변환
		Map<String, List<String>> groupedByPipelineUuid = pipes.stream()
				.collect(Collectors.groupingBy(
						pipe -> pipe.getPipeline().getUuid(), // Pipeline의 UUID로 그룹화
						Collectors.mapping(
								Pipe::getUuid,
								Collectors.toList()
						)
				));

		// 그룹화된 데이터를 PipelineDto 리스트로 변환
		return groupedByPipelineUuid.entrySet().stream()
				.map(entry -> new PipelineModelResponse.PipelineDto(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}

	private void savePipelineObject(String modelUrl, String UUID, PipelineModel pipelineModel) throws IOException, ParseException {
		// Json 정보 추출
		JSONObject jsonObject = getJsonObject(modelUrl);
		JSONArray nodes = (JSONArray) jsonObject.get("nodes");
		Map<String, Pipeline> pipelineMap = new HashMap<>();

		for (int i = 0; i < nodes.size(); i++) {
			JSONObject node = (JSONObject) nodes.get(i);
			String nodeName = (String) node.get("name");

			if (nodeName.startsWith("PipeObj_") && (nodeName.contains("Segment") || nodeName.contains("Connector_"))) {
				String[] parts = nodeName.split("_");
				String pipelineNumber = parts[1];
				Pipeline relatedPipeline = pipelineMap.get(pipelineNumber);

				// 파이프 라인 저장
				if (relatedPipeline == null) {
					relatedPipeline = Pipeline.builder()
							.name("PipeObj_" + pipelineNumber)
							.uuid("PipeObj_" + pipelineNumber + "_" + UUID)
							.pipelineModel(pipelineModel)
							.build();

					pipelineRepository.save(relatedPipeline);
					pipelineMap.put(pipelineNumber, relatedPipeline);
				}

				// 파이프 저장
				Pipe pipe = Pipe.builder()
						.name(nodeName)
						.uuid(nodeName + "_" + UUID)
						.pipeline(relatedPipeline)
						.build();

				pipeRepository.save(pipe);
			}
		}
	}

	private Enterprise getEnterprise(User user) {
		if (user.getRole() == Role.ENTERPRISE) {
			return enterpriseRepository.findByUserId(user.getId());
		} else if (user.getRole() == Role.EMPLOYEE || user.getRole() == Role.ADMIN) {
			return employeeRepository.findByUserId(user.getId()).getEnterprise();
		}

		return null;
	}

	private JSONObject getJsonObject(String modelUrl) throws IOException, ParseException {
		String fileName = modelUrl.split(S3_URL)[1];
		InputStream inputStream = s3Service.download(fileName);
		JSONParser jsonParser = new JSONParser();

		return (JSONObject) jsonParser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	}
}