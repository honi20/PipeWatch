package com.pipewatch.domain.pipeline.service;

import com.pipewatch.domain.pipeline.model.dto.PipelineRequest;
import com.pipewatch.domain.pipeline.model.dto.PipelineResponse;
import com.pipewatch.domain.pipeline.model.entity.Pipeline;
import com.pipewatch.domain.pipeline.model.entity.PipelineProperty;
import com.pipewatch.domain.pipeline.repository.PipelineRepository;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.pipewatch.global.statusCode.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PipelineServiceImpl implements PipelineService {
	private final UserRepository userRepository;
	private final PipelineRepository pipelineRepository;

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
}
