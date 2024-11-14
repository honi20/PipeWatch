package com.pipewatch.domain.pipelineModel.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.pipewatch.global.statusCode.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PipelineModelServiceImpl implements PipelineModelService {
	private final UserRepository userRepository;
	private final PipelineModelRepository pipelineModelRepository;
	private final PipelineModelCustomRepository pipelineModelCustomRepository;
	private final PipelineModelMemoRepository pipelineModelMemoRepository;
	private final PipelineRepository pipelineRepository;
	private final PipeRepository pipeRepository;
	private final S3Service s3Service;
	private final EnterpriseRepository enterpriseRepository;
	private final BuildingRepository buildingRepository;
	private final EmployeeRepository employeeRepository;
	private final PipelineModelApiService pipelineModelApiService;

	@Value("${S3_URL}")
	private String S3_URL;

	@Override
	@Transactional
	public PipelineModelResponse.FileUploadDto uploadImage(Long userId, MultipartFile imageFile) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 관리자 및 기업만 가능
		validateUserRole(user, List.of(Role.USER, Role.EMPLOYEE));

		// 파일 적합성 확인
		if (imageFile.isEmpty() || Objects.isNull(imageFile.getOriginalFilename())) {
			throw new BaseException(FILE_UPLOAD_FAIL);
		}
		validateImageFileExtention(imageFile.getOriginalFilename());

		Enterprise enterprise = getEnterprise(user);

		String modelUuid = java.util.UUID.randomUUID().toString();

		boolean fastApiResponse = pipelineModelApiService.transferImgFile(imageFile, modelUuid);
		if (!fastApiResponse) {
			throw new BaseException(INVALID_IMAGE_FILE_CONTENT);
		}

		// 모델 데이터 생성
		PipelineModel pipelineModel = PipelineModel.builder()
				.user(user)
				.enterprise(enterprise)
				.isCompleted(false)
				.uuid(modelUuid)
				.build();

		pipelineModelRepository.save(pipelineModel);

		return PipelineModelResponse.FileUploadDto.builder()
				.modelId(pipelineModel.getId())
				.modelUuid(pipelineModel.getUuid())
				.build();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void subTransactionForCreateModel(String modelUuid) {
		pipelineModelApiService.createModel(modelUuid);
	}

	@Override
	@Transactional
	public PipelineModelResponse.FileUploadDto uploadFile(Long userId, MultipartFile modelingFile) throws IOException, ParseException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 관리자 및 기업만 가능
		validateUserRole(user, List.of(Role.USER, Role.EMPLOYEE));

		Enterprise enterprise = getEnterprise(user);

		String UUID = java.util.UUID.randomUUID().toString();

		PipelineModel pipelineModel = PipelineModel.builder()
				.user(user)
				.enterprise(enterprise)
				.isCompleted(true)
				.uuid(UUID)
				.build();

		String modelingImgUrl = null;
		if (!modelingFile.isEmpty()) {
			validatePipelineObject(modelingFile);
			modelingImgUrl = s3Service.upload(modelingFile, "PipeLine_" + UUID);
			pipelineModel.updateModelingUrl(modelingImgUrl);
		}

		pipelineModel.updatePreviewImgUrl("https://pipewatch-bucket.s3.ap-northeast-2.amazonaws.com/assets/no_thumbnail.png");
		pipelineModelRepository.save(pipelineModel);

		if (modelingImgUrl != null) {
			savePipelineObject(modelingImgUrl, pipelineModel);
		}

		return PipelineModelResponse.FileUploadDto.builder()
				.modelId(pipelineModel.getId())
				.build();
	}

	@Override
	@Transactional
	public PipelineModelResponse.CreateModelingDto createModeling(PipelineModelRequest.ModelingDto requestDto) throws IOException, ParseException {
		PipelineModel pipelineModel = pipelineModelRepository.findByUuid(requestDto.getModelUuid());

		if (pipelineModel == null) {
			throw new BaseException(PIPELINE_MODEL_NOT_FOUND);
		}

		pipelineModel.updateModelingUrl(requestDto.getModelUrl());
		pipelineModel.updatePreviewImgUrl(requestDto.getPreviewImgUrl());
		pipelineModel.updateIsCompleted(true);
		pipelineModelRepository.save(pipelineModel);

		savePipelineObject(requestDto.getModelUrl(), pipelineModel);

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

		// 생성자만 초기 설정 가능
		if (pipelineModel.getUser().getId() != user.getId()) {
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

		// 파이프라인 모델명 설정
		pipelineModel.updateName(requestDto.getName());
		pipelineModel.updateBuilding(buildingAndFloor);
		pipelineModelRepository.save(pipelineModel);
	}

	@Override
	public PipelineModelResponse.ListDto getModelList(Long userId, String building, Integer floor) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		validateUserRole(user, List.of(Role.USER));

		// 기업 정보 가져오기
		Enterprise enterprise = getEnterprise(user);

		List<PipelineModel> modelList = pipelineModelCustomRepository.findAllByBuildingAndFloor(enterprise, building, floor);

		List<PipelineModelResponse.PipelineModelDto> modelDtos = modelList.stream()
				.map(PipelineModelResponse.PipelineModelDto::fromEntity)
				.collect(Collectors.toList());

		return PipelineModelResponse.ListDto.builder()
				.models(modelDtos)
				.build();
	}

	@Override
	public PipelineModelResponse.DetailDto getModelDetail(Long userId, Long modelId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		validateUserRole(user, List.of(Role.USER));

		// Model
		PipelineModel model = pipelineModelRepository.findById(modelId)
				.orElseThrow(() -> new BaseException(PIPELINE_MODEL_NOT_FOUND));

		// 해당 기업에 속하는 유저만 조회 가능
		validateEnterprise(user, model.getEnterprise());

		// Pipelines Uuid
		List<Pipe> pipes = pipelineModelCustomRepository.findPipeByModel(modelId);
		List<PipelineModelResponse.PipelineDto> pipelines = getPipelineDto(pipes);

		List<Long> defectPipeIds = pipes.stream()
				.filter(Pipe::getHasDefect)
				.map(Pipe::getId)
				.toList();

		return PipelineModelResponse.DetailDto.fromEntity(model, defectPipeIds, pipelines);
	}

	@Override
	@Transactional
	public void modifyModel(Long userId, Long modelId, PipelineModelRequest.ModifyDto requestDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		PipelineModel pipelineModel = pipelineModelRepository.findById(modelId)
				.orElseThrow(() -> new BaseException(PIPELINE_MODEL_NOT_FOUND));

		// 기업이나 관리자 유저만 가능
		validateUserRole(user, List.of(Role.USER, Role.EMPLOYEE));

		// 해당 기업에 속하는 유저만 수정 가능
		validateEnterprise(user, pipelineModel.getEnterprise());

		pipelineModel.updateName(requestDto.getName());

		// 만약 속한 파이프라인이 1개라면, 해당 파이프라인 이름도 변경
		List<Pipeline> pipelines = pipelineRepository.findByPipelineModelId(pipelineModel.getId());

		for (Pipeline pipeline : pipelines) {
			pipeline.updateName(requestDto.getName());
		}
		pipelineRepository.saveAll(pipelines);

		pipelineModelRepository.save(pipelineModel);
	}

	@Override
	@Transactional
	public void modifyThumbnailModel(Long userId, Long modelId, MultipartFile thumbnailFile) throws IOException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		PipelineModel pipelineModel = pipelineModelRepository.findById(modelId)
				.orElseThrow(() -> new BaseException(PIPELINE_MODEL_NOT_FOUND));

		// 생성자만 수정 가능
		if (pipelineModel.getUser().getId() != user.getId()) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		String thumbnailImgUrl = null;
		if (!thumbnailFile.isEmpty()) {
			thumbnailImgUrl = s3Service.upload(thumbnailFile, "Thumbnail_" + pipelineModel.getUuid());
			pipelineModel.updatePreviewImgUrl(thumbnailImgUrl);
			pipelineModelRepository.save(pipelineModel);
		}
	}

	@Override
	@Transactional
	public void deleteModel(Long userId, Long modelId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		PipelineModel pipelineModel = pipelineModelRepository.findById(modelId)
				.orElseThrow(() -> new BaseException(PIPELINE_MODEL_NOT_FOUND));

		// 기업이나 관리자 유저만 가능
		validateUserRole(user, List.of(Role.USER, Role.EMPLOYEE));

		// 해당 기업에 속하는 유저만 수정 가능
		validateEnterprise(user, pipelineModel.getEnterprise());

		// pipeline 삭제
		List<Pipeline> pipelines = pipelineRepository.findByPipelineModelIdOrderByUpdatedAtDesc(modelId);
		pipelineRepository.deleteAll(pipelines);
		// s3 데이터 삭제
		s3Service.fileDelete(pipelineModel.getModelingUrl(), "models/");
		// Model 삭제
		pipelineModelRepository.delete(pipelineModel);
	}

	@Override
	public PipelineModelResponse.MemoListDto getModelMemoList(Long userId, Long modelId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 기업이나 관리자 유저만 가능
		validateUserRole(user, List.of(Role.USER));

		PipelineModel pipelineModel = pipelineModelRepository.findById(modelId)
				.orElseThrow(() -> new BaseException(PIPELINE_MODEL_NOT_FOUND));

		// 해당 기업에 속하는 유저만 수정 가능
		validateEnterprise(user, pipelineModel.getEnterprise());

		List<PipelineModelMemo> memos = pipelineModelMemoRepository.findByPipelineModelId(pipelineModel.getId());
		List<PipelineModelResponse.MemoDto> modelMemoList = memos.stream()
				.map(PipelineModelResponse.MemoDto::fromEntity)
				.toList();

		return PipelineModelResponse.MemoListDto.builder()
				.memoList(modelMemoList)
				.build();
	}

	@Override
	@Transactional
	public PipelineModelResponse.MemoListDto createModelMemo(Long userId, Long modelId, PipelineModelRequest.MemoDto requestDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 기업이나 관리자 유저만 가능
		validateUserRole(user, List.of(Role.USER, Role.EMPLOYEE));

		PipelineModel pipelineModel = pipelineModelRepository.findById(modelId)
				.orElseThrow(() -> new BaseException(PIPELINE_MODEL_NOT_FOUND));

		// 해당 기업에 속하는 유저만 수정 가능
		validateEnterprise(user, pipelineModel.getEnterprise());

		PipelineModelMemo memo = requestDto.toEntity(user, pipelineModel);
		pipelineModelMemoRepository.save(memo);

		List<PipelineModelMemo> memos = pipelineModelMemoRepository.findByPipelineModelId(pipelineModel.getId());
		List<PipelineModelResponse.MemoDto> modelMemoList = memos.stream()
				.map(PipelineModelResponse.MemoDto::fromEntity)
				.toList();

		return PipelineModelResponse.MemoListDto.builder()
				.memoList(modelMemoList)
				.build();
	}

	@Override
	@Transactional
	public void deleteModelMemo(Long userId, Long memoId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		PipelineModelMemo memo = pipelineModelMemoRepository.findById(memoId)
				.orElseThrow(() -> new BaseException(PIPELINE_MODEL_MEMO_NOT_FOUND));

		// 작성자만 삭제 가능
		if (user.getId() != memo.getUser().getId()) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		pipelineModelMemoRepository.delete(memo);
	}

	private List<PipelineModelResponse.PipelineDto> getPipelineDto(List<Pipe> pipes) {
		// pipeline의 name를 기준으로 group화한 후 각 PipelineDto로 변환
		Map<Long, List<PipelineModelResponse.PipeDto>> groupedByPipelineUuid = pipes.stream()
				.collect(Collectors.groupingBy(
						pipe -> pipe.getPipeline().getId(), // Pipeline의 ID로 그룹화
						Collectors.mapping(
								PipelineModelResponse.PipeDto::fromEntity,
								Collectors.toList()
						)
				));

		// 그룹화된 데이터를 PipelineDto 리스트로 변환
		return groupedByPipelineUuid.entrySet().stream()
				.map(entry -> new PipelineModelResponse.PipelineDto(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}

	private void validateImageFileExtention(String filename) {
		int lastDotIndex = filename.lastIndexOf(".");
		if (lastDotIndex == -1) {
			throw new BaseException(FILE_UPLOAD_FAIL);
		}

		String extention = filename.substring(lastDotIndex + 1).toLowerCase();
		List<String> allowedExtentionList = Arrays.asList("mpeg4", "mp4", "mov", "wmv", "png", "jpg", "jpeg");

		if (!allowedExtentionList.contains(extention)) {
			throw new BaseException(INVALID_FILE_EXTENSION);
		}
	}

	private void validatePipelineObject(MultipartFile modelingFile) throws IOException, ParseException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(modelingFile.getInputStream());

		JsonNode nodes = jsonNode.get("meshes");

		// 객체 이름이 하나도 없는 경우
		if (nodes == null || !nodes.isArray() || nodes.isEmpty()) {
			throw new BaseException(INVALID_FILE_CONTENT);
		}
	}

	private void savePipelineObject(String modelingImgUrl, PipelineModel pipelineModel) throws IOException, ParseException {
		// Json 정보 추출
		JSONObject jsonObject = getJsonObject(modelingImgUrl);
		JSONArray nodes = (JSONArray) jsonObject.get("meshes");
		Map<String, Pipeline> pipelineMap = new HashMap<>();

		for (int idx = 0; idx < nodes.size(); idx++) {
			JSONObject node = (JSONObject) nodes.get(idx);
			String nodeName = (String) node.get("name");

			String pipelineNumber = "1";
			String pipeType = "";
			String pipeNumber = "";

			if (isFormattedName(nodeName)) {
				String[] parts = nodeName.split("_");
				pipelineNumber = parts[1];

				if (nodeName.contains("Flange_")) {
					pipeType = parts[4];
					pipeNumber = parts[5];
				} else {
					pipeType = parts[2];
					pipeNumber = parts[3];
				}
			}
			Pipeline relatedPipeline = pipelineMap.get(pipelineNumber);

			// 파이프 라인 저장
			if (relatedPipeline == null) {
				relatedPipeline = Pipeline.builder()
						.name(pipelineModel.getName())
						.pipelineModel(pipelineModel)
						.property(null)
						.build();

				pipelineRepository.save(relatedPipeline);
				pipelineMap.put(pipelineNumber, relatedPipeline);
			}

			// 파이프 저장
			String name = "";
			if (isFormattedName(nodeName)) {
				name = (pipeType.equals("Segment") ? "Pipe" : pipeType) + "_" + pipeNumber;
			} else {
				name = "Pipe_" + (idx + 1);
			}

			Pipe pipe = Pipe.builder()
					.name(name)
					.uuid(nodeName)
					.pipeline(relatedPipeline)
					.build();

			pipeRepository.save(pipe);
		}
	}

	// 허용 안되는 Role 제공
	private void validateUserRole(User user, List<Role> roles) {
		for (Role role : roles) {
			if (user.getRole() == role) {
				throw new BaseException(FORBIDDEN_USER_ROLE);
			}
		}
	}

	// 유저가 해당 기업에 속한는지 확인
	private void validateEnterprise(User user, Enterprise enterprise) {
		if (getEnterprise(user).getId() != enterprise.getId()) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}
	}

	private boolean isFormattedName(String nodeName) {
		if (nodeName.startsWith("PipeObj_")) {
			return nodeName.contains("Segment_") || nodeName.contains("Connector_");
		}
		return false;
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