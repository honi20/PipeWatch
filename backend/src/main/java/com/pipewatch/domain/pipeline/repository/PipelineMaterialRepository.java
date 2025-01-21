package com.pipewatch.domain.pipeline.repository;

import com.pipewatch.domain.pipeline.model.entity.PipelineMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipelineMaterialRepository extends JpaRepository<PipelineMaterial, Long> {
}
