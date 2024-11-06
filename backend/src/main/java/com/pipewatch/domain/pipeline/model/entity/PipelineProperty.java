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
	@Builder.Default
	private String pipeMaterial = "알루미늄";

	@NotNull
	@Builder.Default
	private Double outerDiameter = 150.0;

	@NotNull
	@Builder.Default
	private Double innerDiameter = 10.0;

	@NotNull
	@Builder.Default
	private String fluidMaterial = "물";

	@NotNull
	@Builder.Default
	private Double velocity = 1.0;

	public void updatePipeMaterial(String pipeMaterial) {
		this.pipeMaterial = pipeMaterial;
	}

	public void updateOuterDiameter(Double outerDiameter) {
		this.outerDiameter = outerDiameter;
	}

	public void updateInnerDiameter(Double innerDiameter) {
		this.innerDiameter = innerDiameter;
	}

	public void updateFluidMaterial(String fluidMaterial) {
		this.fluidMaterial = fluidMaterial;
	}

	public void updateVelocity(Double velocity) {
		this.velocity = velocity;
	}
}
