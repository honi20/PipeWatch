package com.pipewatch.domain.pipeline.service;

import com.pipewatch.domain.pipeline.model.dto.PipelineRequest;
import com.pipewatch.domain.pipeline.model.dto.PipelineResponse;
import com.pipewatch.domain.pipeline.model.entity.*;
import com.pipewatch.domain.pipeline.repository.*;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pipewatch.global.statusCode.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PipelineServiceImpl implements PipelineService {
	private final UserRepository userRepository;
	private final PipelineRepository pipelineRepository;
	private final PipeRepository pipeRepository;
	private final PipeMemoRepository pipeMemoRepository;
	private final PipelineMaterialRepository pipelineMaterialRepository;
	private final PipelinePropertyRepository pipelinePropertyRepository;

	@Override
	public PipelineResponse.DetailDto getPipelineDetail(Long userId, Long pipelineId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 기업 관계자만 조회 가능
		validateUserRole(user, List.of(Role.USER));

		Pipeline pipeline = pipelineRepository.findById(pipelineId)
				.orElseThrow(() -> new BaseException(PIPELINE_NOT_FOUND));

		return PipelineResponse.DetailDto.fromEntity(pipeline);
	}

	@Override
	@Transactional
	public void modifyPipeline(Long userId, Long pipelineId, PipelineRequest.ModifyDto requestDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 관리자만 수정 가능
		validateUserRole(user, List.of(Role.USER, Role.EMPLOYEE));

		Pipeline pipeline = pipelineRepository.findById(pipelineId)
				.orElseThrow(() -> new BaseException(PIPELINE_NOT_FOUND));

		pipeline.updateName(requestDto.getName());
		pipelineRepository.save(pipeline);
	}

	@Override
	public List<PipelineResponse.MaterialListDto> getPipeMaterialList() {
		List<PipelineMaterial> materials = pipelineMaterialRepository.findAll();

		// Type별로 분류된 MaterialDto 리스트 생성
		Map<Type, List<PipelineResponse.MaterialDto>> materialsByType = materials.stream()
				.collect(Collectors.groupingBy(
						PipelineMaterial::getType,
						Collectors.mapping(PipelineResponse.MaterialDto::fromEntity, Collectors.toList())
				));

		// MaterialListDto 객체 생성
		List<PipelineResponse.MaterialListDto> materialListDtos = materialsByType.entrySet().stream()
				.map(entry -> PipelineResponse.MaterialListDto.builder()
						.type(entry.getKey())
						.materials(entry.getValue())
						.build()
				).collect(Collectors.toList());

		return materialListDtos;
	}

	@Override
	@Transactional
	public void modifyPipelinePropery(Long userId, Long pipelineId, PipelineRequest.ModifyPropertyDto requestDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 관리자만 수정 가능
		validateUserRole(user, List.of(Role.USER, Role.EMPLOYEE));

		Pipeline pipeline = pipelineRepository.findById(pipelineId)
				.orElseThrow(() -> new BaseException(PIPELINE_NOT_FOUND));

		PipelineMaterial pipeMaterial = pipelineMaterialRepository.findById(requestDto.getPipeMaterialId())
				.orElseThrow(() -> new BaseException(MATERIAL_NOT_FOUND));

		PipelineMaterial fluidMaterial = pipelineMaterialRepository.findById(requestDto.getFluidMaterialId())
				.orElseThrow(() -> new BaseException(MATERIAL_NOT_FOUND));

		PipelineProperty property = pipeline.getProperty();

		// 초기 속성 설정인 경우
		if (property == null) {
			property = PipelineProperty.builder()
					.pipeMaterial(pipeMaterial)
					.fluidMaterial(fluidMaterial)
					.innerDiameter(requestDto.getInnerDiameter())
					.outerDiameter(requestDto.getOuterDiameter())
					.velocity(requestDto.getVelocity())
					.build();

			pipelinePropertyRepository.save(property);
			pipeline.updateProperty(property);
			pipelineRepository.save(pipeline);
		}
		// 변경인 경우
		else {
			property.updatePipeMaterial(pipeMaterial);
			property.updateOuterDiameter(requestDto.getOuterDiameter());
			property.updateInnerDiameter(requestDto.getInnerDiameter());
			property.updateFluidMaterial(fluidMaterial);
			property.updateVelocity(requestDto.getVelocity());

			pipelinePropertyRepository.save(property);
		}
	}

	@Override
	@Transactional
	public PipelineResponse.MemoListDto createPipeMemo(Long userId, Long pipeId, PipelineRequest.CreateMemoDto requestDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 관리자만 수정 가능
		validateUserRole(user, List.of(Role.USER, Role.EMPLOYEE));

		Pipe pipe = pipeRepository.findById(pipeId)
				.orElseThrow(() -> new BaseException(PIPE_NOT_FOUND));

		PipeMemo memo = requestDto.toEntity(user, pipe);

		pipeMemoRepository.save(memo);

		List<PipeMemo> memos = pipeMemoRepository.findByPipeIdOrderByUpdatedAtDesc(pipeId);

		List<PipelineResponse.MemoDto> memoList = memos.stream()
				.map(PipelineResponse.MemoDto::fromEntity).toList();

		return PipelineResponse.MemoListDto.builder()
				.pipeId(pipe.getId())
				.pipeName(pipe.getName())
				.memoList(memoList)
				.build();
	}

	@Override
	public PipelineResponse.PipelineMemoListDto getPipelineMemoList(Long userId, Long pipelineId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		validateUserRole(user, List.of(Role.USER));

		List<Long> pipeIds = pipeRepository.findIdByPipelineId(pipelineId);

		List<PipeMemo> memos = pipeMemoRepository.findByAllPipeId(pipeIds);

		Map<Pipe, List<PipelineResponse.MemoDto>> groupedMemo = memos.stream()
				.collect(Collectors.groupingBy(
						PipeMemo::getPipe,
						Collectors.mapping(PipelineResponse.MemoDto::fromEntity, Collectors.toList()))
				);

		List<PipelineResponse.MemoListDto> memoList = groupedMemo.entrySet().stream()
				.sorted(Comparator.comparing(entry -> entry.getKey().getId()))
				.map(entry -> PipelineResponse.MemoListDto.builder()
						.pipeId(entry.getKey().getId())
						.pipeName(entry.getKey().getName())
						.memoList(entry.getValue())
						.build())
				.toList();

		return PipelineResponse.PipelineMemoListDto.builder()
				.totalMemoList(memoList)
				.build();
	}

	@Override
	public PipelineResponse.MemoListDto getPipeMemoList(Long userId, Long pipeId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		validateUserRole(user, List.of(Role.USER));

		Pipe pipe = pipeRepository.findById(pipeId)
				.orElseThrow(() -> new BaseException(PIPE_NOT_FOUND));

		List<PipeMemo> memos = pipeMemoRepository.findByPipeIdOrderByUpdatedAtDesc(pipeId);

		List<PipelineResponse.MemoDto> memoList = memos.stream()
				.map(PipelineResponse.MemoDto::fromEntity).toList();

		return PipelineResponse.MemoListDto.builder()
				.pipeId(pipe.getId())
				.pipeName(pipe.getName())
				.memoList(memoList)
				.build();
	}

	@Override
	@Transactional
	public void deletePipeMemo(Long userId, Long memoId) {
		PipeMemo memo = pipeMemoRepository.findById(memoId)
				.orElseThrow(() -> new BaseException(PIPE_MEMO_NOT_FOUND));

		// 작성자와 일치하는지 확인
		if (userId != memo.getUser().getId()) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		pipeMemoRepository.delete(memo);
	}

	// 허용 안되는 Role 제공
	private void validateUserRole(User user, List<Role> roles) {
		for (Role role : roles) {
			if (user.getRole() == role) {
				throw new BaseException(FORBIDDEN_USER_ROLE);
			}
		}
	}
}
