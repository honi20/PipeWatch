package com.pipewatch.domain.pipeline.model.dto;

import com.pipewatch.domain.pipeline.model.entity.Pipeline;
import com.pipewatch.domain.pipeline.model.entity.PipelineProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.pipewatch.global.format.DateFormatter.convertToDateFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PipelineResponse {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DetailDto {
		private String name;
		private PropertyDto property;
		private String updatedAt;

		public static DetailDto toDto(Pipeline pipeline) {
			return DetailDto.builder()
					.name(pipeline.getName())
					.property(PropertyDto.toDto(pipeline.getProperty()))
					.updatedAt(convertToDateFormat(pipeline.getUpdated_at()))
					.build();
		}
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemoListDto {
		List<MemoDto> memos;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class PropertyDto {
		private String pipeMaterial;
		private Double outerDiameter;
		private Double innerDiameter;
		private String fluidMaterial;
		private Double velocity;

		public static PropertyDto toDto(PipelineProperty property) {
			return PropertyDto.builder()
					.pipeMaterial(property.getPipeMaterial())
					.outerDiameter(property.getOuterDiameter())
					.innerDiameter(property.getInnerDiameter())
					.fluidMaterial(property.getFluidMaterial())
					.velocity(property.getVelocity())
					.build();
		}
	}

	@Getter
	@AllArgsConstructor
	public static class DefectDto {
		private PositionDto position;
		private String type;
	}

	@Getter
	@AllArgsConstructor
	public static class PositionDto {
		private Double x;
		private Double y;
		private Double z;
	}

	@Getter
	@AllArgsConstructor
	public static class MemoDto {
		private Long memoId;
		private String memo;
		private Creator creator;
		private LocalDateTime createdAt;
	}

	@Getter
	@AllArgsConstructor
	public static class Creator {
		private Long userId;
		private String userName;
	}
}
