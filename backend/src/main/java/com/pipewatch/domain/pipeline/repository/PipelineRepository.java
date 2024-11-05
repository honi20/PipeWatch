package com.pipewatch.domain.pipeline.repository;

import com.pipewatch.domain.pipeline.model.entity.Pipeline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipelineRepository extends JpaRepository<Pipeline, Long> {
}
