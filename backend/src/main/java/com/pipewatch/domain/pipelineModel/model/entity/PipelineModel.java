package com.pipewatch.domain.pipelineModel.model.entity;

import com.pipewatch.domain.enterprise.model.entity.BuildingAndFloor;
import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PipelineModel extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pipeline_model_id")
	private Long id;

	@NotNull
	@Builder.Default
	private String name = "Pipeline Model";

	private String previewImgUrl;

	private String modelingUrl;

	private String uuid;

	@NotNull
	@Builder.Default
	private Boolean isCompleted = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "enterprise_id")
	private Enterprise enterprise;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "building_floor_id")
	private BuildingAndFloor buildingAndFloor;

	public void updateModelingUrl(String modelingUrl) {
		this.modelingUrl = modelingUrl;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateBuilding(BuildingAndFloor buildingAndFloor) {
		this.buildingAndFloor = buildingAndFloor;
	}
}
