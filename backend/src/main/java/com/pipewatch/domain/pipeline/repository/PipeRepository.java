package com.pipewatch.domain.pipeline.repository;

import com.pipewatch.domain.pipeline.model.entity.Pipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PipeRepository extends JpaRepository<Pipe, Long> {
	@Query("SELECT p.id FROM Pipe p WHERE p.pipeline.id = :pipelineId")
	List<Long> findIdByPipelineId(Long pipelineId);
}
