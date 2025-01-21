package com.pipewatch.domain.pipeline.model.entity;

import com.pipewatch.domain.pipelineModel.model.entity.PipelineModel;
import com.pipewatch.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Pipeline extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pipeline_id")
	private Long id;

	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pipeline_model_id")
	private PipelineModel pipelineModel;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "pipeline_property")
	private PipelineProperty property;

	@OneToMany(mappedBy = "pipeline", cascade = CascadeType.REMOVE)
	private List<Pipe> pipeList = new ArrayList<>();

	public void updateName(String name) {
		this.name = name;
	}

	public void updateProperty(PipelineProperty property) {
		this.property = property;
	}
}
