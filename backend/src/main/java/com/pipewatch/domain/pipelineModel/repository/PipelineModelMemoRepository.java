package com.pipewatch.domain.pipelineModel.repository;

import com.pipewatch.domain.pipelineModel.model.entity.PipelineModelMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PipelineModelMemoRepository extends JpaRepository<PipelineModelMemo, Long> {
	@Query("select pmm from PipelineModelMemo pmm where pmm.pipelineModelMemo.id = :modelId order by pmm.updatedAt")
	List<PipelineModelMemo> findByPipelineModelIdOrder(Long modelId);
}
