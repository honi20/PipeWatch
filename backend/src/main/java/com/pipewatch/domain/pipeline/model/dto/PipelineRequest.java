package com.pipewatch.domain.pipeline.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
		@Schema(description = "변경할 파이프라인 이름", example = "New Pipeline Name")
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
