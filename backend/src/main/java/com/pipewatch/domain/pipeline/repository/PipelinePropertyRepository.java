package com.pipewatch.domain.pipeline.repository;

import com.pipewatch.domain.pipeline.model.entity.PipelineProperty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipelinePropertyRepository extends JpaRepository<PipelineProperty, Long> {
}
