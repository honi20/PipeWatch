package com.pipewatch.domain.pipelineModel.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
		@Schema(description = "파이프라인 모델명", example = "Pipeline Model")
		private String name;
		@Schema(description = "건물명", example = "역삼 멀티캠퍼스")
		private String building;
		@Schema(description = "층수", example = "14")
		private Integer floor;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ModifyDto {
		@Schema(description = "변경된 파이프라인 모델명", example = "New Pipeline Model")
		private String name;
		@Schema(description = "파이프라인 모델 설명", example = "New Description")
		private String description;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ModelingDto {
		@Schema(description = "생성자 UUID", example = "8e7dfbe3-aeca-4392-8d90-c1d3ae4fd35f")
		private String userUuid;
		@Schema(description = "썸네일 이미지 URL", example = "s3 thumbnail url")
		private String previewImgUrl;
		@Schema(description = "모델링 파일 URL", example = "s3 modeling file url")
		private String modelUrl;
	}
}
