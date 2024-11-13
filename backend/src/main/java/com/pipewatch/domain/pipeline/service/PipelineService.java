package com.pipewatch.domain.pipeline.service;

import com.pipewatch.domain.pipeline.model.dto.PipelineRequest;
import com.pipewatch.domain.pipeline.model.dto.PipelineResponse;

import java.util.List;

public interface PipelineService {
	PipelineResponse.DetailDto getPipelineDetail(Long userId, Long pipelineId);

	void modifyPipeline(Long userId, Long pipelineId, PipelineRequest.ModifyDto requestDto);

	List<PipelineResponse.MaterialListDto> getPipeMaterialList();
	
	void modifyPipelinePropery(Long userId, Long pipelineId, PipelineRequest.ModifyPropertyDto requestDto);

	PipelineResponse.MemoListDto createPipeMemo(Long userId, Long pipeId, PipelineRequest.CreateMemoDto requestDto);

	PipelineResponse.PipelineMemoListDto getPipelineMemoList(Long userId, Long pipelineId);

	PipelineResponse.MemoListDto getPipeMemoList(Long userId, Long pipeId);

	void deletePipeMemo(Long userId, Long memoId);

	void modifyPipeDefect(Long userId, Long pipeId);
}
