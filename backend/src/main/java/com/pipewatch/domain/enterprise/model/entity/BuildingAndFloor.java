package com.pipewatch.domain.enterprise.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BuildingAndFloor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "building_floor_id")
	private Long id;

	private String name;

	private Integer floor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "enterprise_id")
	private Enterprise enterprise;
}