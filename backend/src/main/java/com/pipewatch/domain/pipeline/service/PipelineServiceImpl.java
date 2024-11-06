package com.pipewatch.domain.pipeline.service;

import com.pipewatch.domain.pipeline.model.dto.PipelineResponse;
import com.pipewatch.domain.pipeline.model.entity.Pipeline;
import com.pipewatch.domain.pipeline.repository.PipelineRepository;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
