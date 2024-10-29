package com.pipewatch.domain.pipeline.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PipelineRequest {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ModifyDto {
		private String name;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ModifyPropertiesDto {
		private String pipeMaterial;
		private Double outerDiameter;
		private Double innerDiameter;
		private String fluidMaterial;
		private Double velocity;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateMemoDto {
		private String memo;
	}
}
