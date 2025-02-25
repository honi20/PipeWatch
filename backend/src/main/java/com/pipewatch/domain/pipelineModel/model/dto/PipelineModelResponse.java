package com.pipewatch.domain.pipelineModel.model.dto;

import com.pipewatch.domain.pipeline.model.entity.Pipe;
import com.pipewatch.domain.pipelineModel.model.entity.PipelineModel;
import com.pipewatch.domain.pipelineModel.model.entity.PipelineModelMemo;
import lombok.*;

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

		public static PipelineModelDto fromEntity(PipelineModel model) {
			return PipelineModelDto.builder()
					.modelId(model.getId())
					.name(model.getName())
					.previewUrl(model.getPreviewImgUrl())
					.building(model.getBuildingAndFloor() == null ? null : model.getBuildingAndFloor().getName())
					.floor(model.getBuildingAndFloor() == null ? null : model.getBuildingAndFloor().getFloor())
					.updatedAt(convertToDateFormat(model.getUpdatedAt()))
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
		private String modelUuid;
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
		private String modelingUrl;
		private String building;
		private Integer floor;
		private Boolean isCompleted;
		private String updatedAt;
		private List<Long> defectPipeList;
		private List<PipelineDto> pipelines;
		private Creator creator;

		public static DetailDto fromEntity(PipelineModel model, List<Long> defectList, List<PipelineDto> pipelines) {
			return DetailDto.builder()
					.name(model.getName())
					.modelingUrl(model.getModelingUrl())
					.building(model.getBuildingAndFloor() == null ? null : model.getBuildingAndFloor().getName())
					.floor(model.getBuildingAndFloor() == null ? null : model.getBuildingAndFloor().getFloor())
					.isCompleted(model.getIsCompleted())
					.updatedAt(convertToDateFormat(model.getUpdatedAt()))
					.defectPipeList(defectList)
					.pipelines(pipelines)
					.creator(new Creator(model.getUser().getUuid(), model.getUser().getName()))
					.build();
		}
	}

	@Getter
	@AllArgsConstructor
	public static class PipelineDto {
		private Long pipelineId;
		private List<PipeDto> pipes;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class PipeDto {
		private Long pipeId;
		private String pipeUuid;

		public static PipeDto fromEntity(Pipe pipe) {
			return PipeDto.builder()
					.pipeId(pipe.getId())
					.pipeUuid(pipe.getUuid())
					.build();
		}
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemoListDto {
		private List<MemoDto> memoList;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class MemoDto {
		private Long memoId;
		private String memo;
		private Creator writer;
		private String createdAt;

		public static MemoDto fromEntity(PipelineModelMemo memo) {
			return MemoDto.builder()
					.memoId(memo.getId())
					.memo(memo.getMemo())
					.writer(new Creator(memo.getUser().getUuid(), memo.getUser().getName()))
					.createdAt(convertToDateFormat(memo.getCreatedAt()))
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
