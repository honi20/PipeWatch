package com.pipewatch.domain.pipeline.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PipelineMaterial {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pipeline_material_id")
	private Long id;

	private String koreanName;

	private String englishName;

	@Enumerated(EnumType.STRING)
	private Type type;
}
