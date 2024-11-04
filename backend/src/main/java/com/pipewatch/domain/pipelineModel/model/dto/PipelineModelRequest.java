package com.pipewatch.domain.pipelineModel.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PipelineModelRequest {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InitDto {
		private String name;
		private String building;
		private Integer floor;
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

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ModelingDto {
		private String userUuid;
		private String previewImgUrl;
		private String modelUrl;
	}
}
