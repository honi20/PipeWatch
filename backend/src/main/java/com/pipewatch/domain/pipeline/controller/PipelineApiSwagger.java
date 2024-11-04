package com.pipewatch.domain.pipeline.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pipeline API", description = "Pipeline API Document")
public interface PipelineApiSwagger {
	@GetMapping("/{pipelineUuid}")
	@Operation(summary = "파이프라인 상세조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파이프라인 상세조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"단일 파이프라인 조회에 성공했습니다.\"}," +
							"\"body\": {\n" +
							"    \"name\": \"Pipeline1\",\n" +
							"    \"updatedAt\": \"2024-11-03T00:14:34.022156\",\n" +
							"    \"property\": {\n" +
							"      \"pipeMaterial\": \"Aluminum\",\n" +
							"      \"outerDiameter\": 150,\n" +
							"      \"innerDiameter\": 10.3,\n" +
							"      \"fluidMaterial\": \"Water\",\n" +
							"      \"velocity\": 1\n" +
							"    },\n" +
							"    \"defects\": [\n" +
							"      {\n" +
							"        \"position\": {\n" +
							"          \"x\": 10.12,\n" +
							"          \"y\": 22.11,\n" +
							"          \"z\": 31.49\n" +
							"        },\n" +
							"        \"type\": \"CRACK\"\n" +
							"      },\n" +
							"      {\n" +
							"        \"position\": {\n" +
							"          \"x\": 230.12,\n" +
							"          \"y\": 122.11,\n" +
							"          \"z\": 341.49\n" +
							"        },\n" +
							"        \"type\": \"CORROSION\"\n" +
							"      }\n" +
							"    ]\n" +
							"  }}")}
			))
	})
	ResponseEntity<?> pipelineDetail(
			@Schema(description = "파이프라인 Uuid", example = "PipeObj_1_fjkelsfncjs")
			@PathVariable String pipelineUuid);

	@PutMapping("/{pipelineUuid}")
	@Operation(summary = "파이프라인 기본 정보 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파이프라인 기본 정보 수정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"단일 파이프라인 기본 정보 수정에 성공했습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> pipelineModify(
			@Schema(description = "파이프라인 Uuid", example = "PipeObj_1_fjkelsfncjs")
			@PathVariable String pipelineUuid);

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
