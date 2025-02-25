package com.pipewatch.domain.pipeline.repository;

import com.pipewatch.domain.pipeline.model.entity.Pipeline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PipelineRepository extends JpaRepository<Pipeline, Long> {
	List<Pipeline> findByPipelineModelIdOrderByUpdatedAtDesc(Long modelId);

	List<Pipeline> findByPipelineModelId(Long id);
}
