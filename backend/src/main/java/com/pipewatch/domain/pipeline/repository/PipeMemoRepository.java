package com.pipewatch.domain.pipeline.repository;

import com.pipewatch.domain.pipeline.model.entity.PipeMemo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PipeMemoRepository extends JpaRepository<PipeMemo, Long> {
	List<PipeMemo> findByPipeId(Long pipeId);

	@Query("SELECT pm FROM PipeMemo pm WHERE pm.pipe.id IN :pipeIds")
	List<PipeMemo> findByAllPipeId(@Param("pipeIds") List<Long> pipeIds);
}
