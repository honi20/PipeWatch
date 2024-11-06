package com.pipewatch.domain.pipeline.repository;

import com.pipewatch.domain.pipeline.model.entity.PipeMemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PipeMemoRepository extends JpaRepository<PipeMemo, Long> {
	List<PipeMemo> findByPipeId(Long pipeId);
}
