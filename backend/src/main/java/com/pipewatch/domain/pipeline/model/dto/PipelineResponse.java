package com.pipewatch.domain.pipeline.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
		private LocalDateTime updatedAt;
		private PropertyDto property;
		private List<DefectDto> defects;
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
	@AllArgsConstructor
	public static class PropertyDto {
		private String pipeMaterial;
		private Double outerDiameter;
		private Double innerDiameter;
		private String fluidMaterial;
		private Double velocity;
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
		private PositionDto position;
		private Creator creator;
	}

	@Getter
	@AllArgsConstructor
	public static class Creator {
		private Long userId;
		private String userName;
	}
}
