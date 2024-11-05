package com.pipewatch.domain.pipelineModel.model.dto;

import com.pipewatch.domain.pipelineModel.model.entity.PipelineModel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.pipewatch.global.format.DateFormatter.convertToDateFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PipelineModelResponse {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ListDto {
		private List<PipelineModelDto> models;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class PipelineModelDto {
		private Long modelId;
		private String name;
		private String previewUrl;
		private String building;
		private Integer floor;
		private String updatedAt;

		public static PipelineModelDto toDto(PipelineModel model) {
			return PipelineModelDto.builder()
					.modelId(model.getId())
					.name(model.getName())
					.previewUrl(model.getPreviewImgUrl())
					.building(model.getBuildingAndFloor().getName())
					.floor(model.getBuildingAndFloor().getFloor())
					.updatedAt(convertToDateFormat(model.getUpdated_at()))
					.build();
		}
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
	public static class CreateModelingDto {
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
