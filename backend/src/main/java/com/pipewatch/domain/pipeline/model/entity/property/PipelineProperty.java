package com.pipewatch.domain.pipeline.model.entity.property;

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

	private Integer outerDiameter;

	private Integer innerDiameter;

	private Double velocity;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "pipe_material_id", name = "pipe_material_id")
	private PipeMaterial pipeMaterial;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "pipe_material_id", name = "fluid_material_id")
	private PipeMaterial fluidMaterial;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fluid_id")
	private Fluid fluid;
}
