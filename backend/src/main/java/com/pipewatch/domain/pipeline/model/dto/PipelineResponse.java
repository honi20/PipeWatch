package com.pipewatch.domain.pipeline.model.dto;

import com.pipewatch.domain.pipeline.model.entity.*;
import lombok.*;

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

		public static DetailDto fromEntity(Pipeline pipeline) {
			return DetailDto.builder()
					.name(pipeline.getName())
					.property(PropertyDto.fromEntity(pipeline.getProperty()))
					.updatedAt(convertToDateFormat(pipeline.getUpdated_at()))
					.build();
		}
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MaterialListDto {
		private Type type;
		private List<MaterialDto> materials;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class MaterialDto {
		private Long materialId;
		private String koreanName;
		private String englishName;

		public static MaterialDto fromEntity(PipelineMaterial material) {
			return MaterialDto.builder()
					.materialId(material.getId())
					.koreanName(material.getKoreanName())
					.englishName(material.getEnglishName())
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
		private MaterialDto pipeMaterial;
		private Double outerDiameter;
		private Double innerDiameter;
		private MaterialDto fluidMaterial;
		private Double velocity;

		public static PropertyDto fromEntity(PipelineProperty property) {
			PipelineMaterial pipeMaterial = property.getPipeMaterial();
			PipelineMaterial fluidMaterial = property.getFluidMaterial();

			return PropertyDto.builder()
					.pipeMaterial(pipeMaterial == null ? null :
							new MaterialDto(pipeMaterial.getId(), pipeMaterial.getKoreanName(), pipeMaterial.getEnglishName()))
					.outerDiameter(property.getOuterDiameter())
					.innerDiameter(property.getInnerDiameter())
					.fluidMaterial(fluidMaterial == null ? null :
							new MaterialDto(fluidMaterial.getId(), fluidMaterial.getKoreanName(), fluidMaterial.getEnglishName()))
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

		public static MemoDto fromEntity(PipeMemo memo) {
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
