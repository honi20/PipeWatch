package com.pipewatch.domain.pipeline.model.dto;

import com.pipewatch.domain.pipeline.model.entity.PipeMemo;
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
	public static class PipelineMemoListDto {
		List<MemoListDto> totalMemoList;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemoListDto {
		private Long pipeId;
		private String pipeName;
		private List<MemoDto> memoList;
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
	@Builder
	@AllArgsConstructor
	public static class MemoDto {
		private Long memoId;
		private String memo;
		private Creator creator;
		private String createdAt;

		public static MemoDto toDto(PipeMemo memo) {
			return MemoDto.builder()
					.memoId(memo.getId())
					.memo(memo.getMemo())
					.creator(new Creator(memo.getUser().getUuid(), memo.getUser().getName()))
					.createdAt(convertToDateFormat(memo.getCreated_at()))
					.build();
		}
	}

	@Getter
	@AllArgsConstructor
	public static class Creator {
		private String userUuid;
		private String userName;
	}
}
