package com.pipewatch.domain.pipeline.service;

import com.pipewatch.domain.pipeline.model.dto.PipelineResponse;

public interface PipelineService {
	PipelineResponse.DetailDto getPipelineDetail(Long userId, Long pipelineId);
}
