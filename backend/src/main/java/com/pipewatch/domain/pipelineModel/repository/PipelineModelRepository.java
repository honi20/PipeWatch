package com.pipewatch.domain.pipelineModel.repository;

import com.pipewatch.domain.pipelineModel.model.entity.PipelineModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipelineModelRepository extends JpaRepository<PipelineModel, Long> {
	PipelineModel findByUuid(String modelUuid);
}
