package com.pipewatch.domain.pipeline.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PipelineProperty {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pipeline_prop_id")
	private Long id;

	@NotNull
	private Double outerDiameter;

	@NotNull
	private Double innerDiameter;

	@NotNull
	private Double velocity;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pipe_material_id")
	private PipelineMaterial pipeMaterial;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fluid_material_id")
	private PipelineMaterial fluidMaterial;

	public void updatePipeMaterial(PipelineMaterial pipeMaterial) {
		this.pipeMaterial = pipeMaterial;
	}

	public void updateOuterDiameter(Double outerDiameter) {
		this.outerDiameter = outerDiameter;
	}

	public void updateInnerDiameter(Double innerDiameter) {
		this.innerDiameter = innerDiameter;
	}

	public void updateFluidMaterial(PipelineMaterial fluidMaterial) {
		this.fluidMaterial = fluidMaterial;
	}

	public void updateVelocity(Double velocity) {
		this.velocity = velocity;
	}
}
