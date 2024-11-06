package com.pipewatch.domain.pipeline.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PipelineProperty {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pipeline_prop_id")
	private Long id;

	private String pipe_material;

	private Integer outerDiameter;

	private Integer innerDiameter;

	private String fluid_material;

	private Double velocity;
}
