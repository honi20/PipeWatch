package com.pipewatch.domain.pipeline.model.entity.property;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Fluid {
	@Id
	@GeneratedValue
	@Column(name = "fluid_id")
	private Long id;

	private String name;

	private Double density;

	private Double viscosity;

	private Double specificHeatCapacity;

	private Double thermalConductivity;

	private Double pressure;
}
