package com.pipewatch.domain.enterprise.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BuildingAndFloor {
	@Id
	@GeneratedValue
	@Column(name = "building_floor_id")
	private Long id;

	private String name;

	private Integer floor;

	@ManyToOne
	@JoinColumn(name = "enterprise_id")
	private Enterprise enterprise;
}