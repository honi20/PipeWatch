package com.pipewatch.domain.pipeline.repository;

import com.pipewatch.domain.pipeline.model.entity.Pipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipeRepository extends JpaRepository<Pipe, Long> {
}
