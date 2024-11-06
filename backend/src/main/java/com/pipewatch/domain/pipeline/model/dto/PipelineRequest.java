package com.pipewatch.domain.pipeline.model.dto;

import com.pipewatch.domain.pipeline.model.entity.Pipe;
import com.pipewatch.domain.pipeline.model.entity.PipeMemo;
import com.pipewatch.domain.pipeline.model.entity.PipelineProperty;
import com.pipewatch.domain.user.model.entity.User;
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
		@Schema(description = "변경된 파이프라인 이름", example = "New Pipeline Name")
		private String name;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ModifyPropertyDto {
		@Schema(description = "변경된 파이프 재질", example = "알루미늄")
		private String pipeMaterial;
		@Schema(description = "변경된 파이프 외경", example = "150.0")
		private Double outerDiameter;
		@Schema(description = "변경된 파이프 내경", example = "10.3")
		private Double innerDiameter;
		@Schema(description = "변경된 유체 재질", example = "물")
		private String fluidMaterial;
		@Schema(description = "변경된 유체 유속", example = "1.5")
		private Double velocity;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateMemoDto {
		private String memo;
		private Boolean hasDefect;

		public PipeMemo toEntity(User user, Pipe pipe) {
			return PipeMemo.builder()
					.memo(this.memo)
					.hasDefect(this.hasDefect)
					.user(user)
					.pipe(pipe)
					.build();
		}
	}
}
