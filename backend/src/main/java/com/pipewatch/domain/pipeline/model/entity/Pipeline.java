package com.pipewatch.domain.pipeline.model.entity;

import com.pipewatch.domain.pipelineModel.model.entity.PipelineModel;
import com.pipewatch.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Pipeline extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pipeline_id")
	private Long id;

	private String name;

	private String uuid;

	private String pipelineUrl;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "pipeline_model_id")
	private PipelineModel pipelineModel;
}
