package com.pipewatch.domain.pipeline.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Pipe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pipe_id")
	private Long id;

	private String name;

	private String uuid;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "pipeline_id")
	private Pipeline pipeline;
}
