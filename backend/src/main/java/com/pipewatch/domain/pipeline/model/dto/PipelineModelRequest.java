package com.pipewatch.domain.pipeline.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PipelineModelRequest {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UpdateDto {
		private String name;
		private String building;
		private String floor;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ModifyDto {
		private String name;
		private String description;
	}
}
