package com.pipewatch.domain.pipeline.service;

import com.pipewatch.domain.pipeline.model.dto.PipelineRequest;
import com.pipewatch.domain.pipeline.model.dto.PipelineResponse;
import com.pipewatch.domain.pipeline.model.entity.Pipe;
import com.pipewatch.domain.pipeline.model.entity.PipeMemo;
import com.pipewatch.domain.pipeline.model.entity.Pipeline;
import com.pipewatch.domain.pipeline.model.entity.PipelineProperty;
import com.pipewatch.domain.pipeline.repository.PipeMemoRepository;
import com.pipewatch.domain.pipeline.repository.PipeRepository;
import com.pipewatch.domain.pipeline.repository.PipelineRepository;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.pipewatch.global.statusCode.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PipelineServiceImpl implements PipelineService {
	private final UserRepository userRepository;
	private final PipelineRepository pipelineRepository;
	private final PipeRepository pipeRepository;
	private final PipeMemoRepository pipeMemoRepository;

	@Override
	public PipelineResponse.DetailDto getPipelineDetail(Long userId, Long pipelineId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 기업 관계자만 조회 가능
		if (user.getRole() == Role.USER) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		Pipeline pipeline = pipelineRepository.findById(pipelineId)
				.orElseThrow(() -> new BaseException(PIPELINE_NOT_FOUND));

		return PipelineResponse.DetailDto.toDto(pipeline);
	}

	@Override
	@Transactional
	public void modifyPipeline(Long userId, Long pipelineId, PipelineRequest.ModifyDto requestDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 관리자만 수정 가능
		if (user.getRole() == Role.USER || user.getRole() == Role.EMPLOYEE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		Pipeline pipeline = pipelineRepository.findById(pipelineId)
				.orElseThrow(() -> new BaseException(PIPELINE_NOT_FOUND));

		pipeline.updateName(requestDto.getName());
		pipelineRepository.save(pipeline);
	}

	@Override
	@Transactional
	public void modifyPipelinePropery(Long userId, Long pipelineId, PipelineRequest.ModifyPropertyDto requestDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 관리자만 수정 가능
		if (user.getRole() == Role.USER || user.getRole() == Role.EMPLOYEE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		Pipeline pipeline = pipelineRepository.findById(pipelineId)
				.orElseThrow(() -> new BaseException(PIPELINE_NOT_FOUND));

		PipelineProperty property = pipeline.getProperty();
		property.updatePipeMaterial(requestDto.getPipeMaterial());
		property.updateOuterDiameter(requestDto.getOuterDiameter());
		property.updateInnerDiameter(requestDto.getInnerDiameter());
		property.updateFluidMaterial(requestDto.getFluidMaterial());
		property.updateVelocity(requestDto.getVelocity());

		pipelineRepository.save(pipeline);
	}

	@Override
	@Transactional
	public void createPipeMemo(Long userId, Long pipeId, PipelineRequest.CreateMemoDto requestDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 관리자만 수정 가능
		if (user.getRole() == Role.USER || user.getRole() == Role.EMPLOYEE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		Pipe pipe = pipeRepository.findById(pipeId)
				.orElseThrow(() -> new BaseException(PIPE_NOT_FOUND));

		PipeMemo memo = requestDto.toEntity(user, pipe);

		pipeMemoRepository.save(memo);
	}

	@Override
	public PipelineResponse.PipelineMemoListDto getPipelineMemoList(Long userId, Long pipelineId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		if (user.getRole() == Role.USER) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		List<Long> pipeIds = pipeRepository.findIdByPipelineId(pipelineId);

		List<PipeMemo> memos = pipeMemoRepository.findByAllPipeId(pipeIds);

		Map<Pipe, List<PipelineResponse.MemoDto>> groupedMemo = memos.stream()
				.collect(Collectors.groupingBy(
						PipeMemo::getPipe,
						Collectors.mapping(PipelineResponse.MemoDto::toDto, Collectors.toList()))
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

		if (user.getRole() == Role.USER) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		Pipe pipe = pipeRepository.findById(pipeId)
				.orElseThrow(() -> new BaseException(PIPE_NOT_FOUND));

		List<PipeMemo> memos = pipeMemoRepository.findByPipeId(pipeId);

		List<PipelineResponse.MemoDto> memoList = memos.stream()
				.map(PipelineResponse.MemoDto::toDto).toList();

		return PipelineResponse.MemoListDto.builder()
				.pipeId(pipe.getId())
				.pipeName(pipe.getName())
				.memoList(memoList)
				.build();
	}
}
