package com.pipewatch.domain.pipeline.controller;

import com.pipewatch.domain.pipeline.model.dto.PipelineRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pipeline API", description = "Pipeline API Document")
public interface PipelineApiSwagger {
	@GetMapping("/{pipelineId}")
	@Operation(summary = "파이프라인 상세조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파이프라인 상세조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"단일 파이프라인 조회에 성공했습니다.\"}," +
							"\"body\": {\n" +
							"    \"name\": \"PipeLine_1\",\n" +
							"    \"property\": {\n" +
							"      \"pipeMaterial\": \"알루미늄\",\n" +
							"      \"outerDiameter\": 150,\n" +
							"      \"innerDiameter\": 10,\n" +
							"      \"fluidMaterial\": \"물\",\n" +
							"      \"velocity\": 1\n" +
							"    },\n" +
							"    \"updatedAt\": \"2024-11-06 13:57:07\"\n" +
							"  }}")}
			))
	})
	ResponseEntity<?> pipelineDetail(@AuthenticationPrincipal Long userId,
									 @Schema(description = "파이프라인 Id", example = "1")
									 @PathVariable Long pipelineId);

	@PutMapping("/{pipelineId}")
	@Operation(summary = "파이프라인 기본 정보 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파이프라인 기본 정보 수정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"단일 파이프라인 기본 정보 수정에 성공했습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> pipelineModify(@AuthenticationPrincipal Long userId,
									 @Schema(description = "파이프라인 Id", example = "1")
									 @PathVariable Long pipelineId, @RequestBody PipelineRequest.ModifyDto requestDto);

	@PutMapping("/{pipelineUuid}/properties")
	@Operation(summary = "파이프라인 속성 정보 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파이프라인 속성 정보 수정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"단일 파이프라인 속성 정보 수정에 성공했습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> pipelinePropertyModify(
			@Schema(description = "파이프라인 Uuid", example = "PipeObj_1_fjkelsfncjs")
			@PathVariable String pipelineUuid);

	@GetMapping("/pipes/{pipeUuid}")
	@Operation(summary = "파이프 메모 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파이프 메모 리스트 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"파이프 메모 리스트 조회에 성공했습니다.\"}," +
							"\"body\": {\n" +
							"    \"memos\": [\n" +
							"      {\n" +
							"        \"memoId\": 1,\n" +
							"        \"memo\": \"부식 위험 있음\",\n" +
							"        \"creator\": {\n" +
							"          \"userId\": 1,\n" +
							"          \"userName\": \"김싸피\"\n" +
							"        },\n" +
							"        \"createdAt\": \"2024-11-03T00:14:34.157712\"\n" +
							"      },\n" +
							"      {\n" +
							"        \"memoId\": 2,\n" +
							"        \"memo\": \"다음주에 점검함\",\n" +
							"        \"creator\": {\n" +
							"          \"userId\": 2,\n" +
							"          \"userName\": \"최싸피\"\n" +
							"        },\n" +
							"        \"createdAt\": \"2024-11-03T00:14:34.157751\"\n" +
							"      }\n" +
							"    ]\n" +
							"  }}")}
			))
	})
	ResponseEntity<?> pipeDetail(
			@Schema(description = "파이프 Uuid", example = "PipeObj_1_Segment_1_fjkelsfncjs")
			@PathVariable String pipeUuid);

	@PostMapping("/pipes/{pipeUuid}")
	@Operation(summary = "파이프 메모 생성")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "파이프 메모 생성 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 201, \"message\": \"파이프 메모가 생성되었습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> pipeMemoCreate(
			@Schema(description = "파이프 Uuid", example = "PipeObj_1_Segment_1_fjkelsfncjs")
			@PathVariable String pipeUuid);

	@DeleteMapping("/pipes/{memoId}")
	@Operation(summary = "파이프 메모 삭제")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "파이프 메모 삭제 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 204, \"message\": \"메모 삭제에 성공했습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> pipeMemoCreate(
			@Schema(description = "메모 ID", example = "1")
			@PathVariable Long memoId);
}
