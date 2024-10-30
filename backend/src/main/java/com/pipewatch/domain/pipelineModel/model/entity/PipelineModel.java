package com.pipewatch.domain.pipelineModel.model.entity;

import com.pipewatch.domain.enterprise.model.entity.BuildingAndFloor;
import com.pipewatch.domain.user.model.entity.User;
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
public class PipelineModel extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pipeline_model_id")
	private Long id;

	private String name;

	private String description;

	private String previewImgUri;

	private String pipelineModelingUrl;

	private Boolean isCompleted;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "building_floor_id")
	private BuildingAndFloor buildingAndFloor;
}
