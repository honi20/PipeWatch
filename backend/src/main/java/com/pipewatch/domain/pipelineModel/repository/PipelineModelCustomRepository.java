package com.pipewatch.domain.pipelineModel.repository;

import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import com.pipewatch.domain.pipeline.model.entity.Pipe;
import com.pipewatch.domain.pipelineModel.model.entity.PipelineModel;

import java.util.List;

public interface PipelineModelCustomRepository {
	List<PipelineModel> findAllByBuildingAndFloor(Enterprise enterprise, String building, Integer floor);

	List<Pipe> findPipeByModel(Long modelId);
}
