package com.pipewatch.domain.pipelineModel.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PipelineModelResponse {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ListDto {
		private List<BuildingListDto> buildings;
	}

	@Getter
	@AllArgsConstructor
	public static class BuildingListDto {
		private String building;
		private List<FloorListDto> floors;
	}

	@Getter
	@AllArgsConstructor
	public static class FloorListDto {
		private Integer floor;
		private List<PipelineModelDto> models;
	}

	@Getter
	@AllArgsConstructor
	public static class PipelineModelDto {
		private Long modelId;
		private String name;
		private String previewUrl;
		private LocalDateTime updatedAt;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class FileUploadDto {
		private Long modelId;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DetailDto {
		private String name;
		private String description;
		private String modelingUrl;
		private Boolean isCompleted;
		private LocalDateTime updatedAt;
		private Creator creator;
	}

	@Getter
	@AllArgsConstructor
	public static class Creator {
		private Long userId;
		private String userName;
	}
}
