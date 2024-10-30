package com.pipewatch.domain.pipeline.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Entity
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PipelineDefect {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pipeline_defect_id")
	private Long id;

	private BigDecimal x;

	private BigDecimal y;

	private BigDecimal z;

	private DefectType type;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "pipeline_id")
	private Pipeline pipeline;
}
