package com.pipewatch.domain.pipeline.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pipeline_id")
	private Pipeline pipeline;
}
