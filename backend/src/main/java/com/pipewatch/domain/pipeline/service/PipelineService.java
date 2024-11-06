package com.pipewatch.domain.pipeline.service;

import com.pipewatch.domain.pipeline.model.dto.PipelineRequest;
import com.pipewatch.domain.pipeline.model.dto.PipelineResponse;

public interface PipelineService {
	PipelineResponse.DetailDto getPipelineDetail(Long userId, Long pipelineId);

	void modifyPipeline(Long userId, Long pipelineId, PipelineRequest.ModifyDto requestDto);

	void modifyPipelinePropery(Long userId, Long pipelineId, PipelineRequest.ModifyPropertyDto requestDto);

	void createPipeMemo(Long userId, Long pipeId, PipelineRequest.CreateMemoDto requestDto);

	PipelineResponse.MemoListDto getPipeMemoList(Long userId, Long pipeId);
}
