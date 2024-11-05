package com.pipewatch.domain.pipelineModel.controller;

import com.pipewatch.domain.pipelineModel.model.dto.PipelineModelRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Pipeline Model API", description = "Pipeline Model API Document")
public interface PipelineModelApiSwagger {
	@PostMapping(value = "/upload-file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@Operation(summary = "GLTF File 업로드")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "GLTF File 업로드 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 201, \"message\": \"GLTF 파일 업로드에 성공했습니다. 파이프라인 모델이 생성되었습니다.\"},\n\"body\": {\"modelId\": 1}}")}
			))
	})
	ResponseEntity<?> fileUpload(@AuthenticationPrincipal Long userId, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException, ParseException;

	@PostMapping("/modeling")
	@Operation(summary = "파이프라인 모델링 생성", description = "Fast API로부터 완성된 glft 모델링 파일 받는 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "파이프라인 모델링 생성 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 201, \"message\": \"파이프라인 모델링이 생성되었습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> modelingCreate(@RequestBody PipelineModelRequest.ModelingDto requestDto) throws IOException, ParseException;

	@PatchMapping("/init/{modelId}")
	@Operation(summary = "파이프라인 모델 초기 정보 설정", description = "모델 생성 시 입력되는 초기 정보 (모델명, 건물, 층)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파이프라인 모델 초기 정보 설정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"모델 초기 정보 설정에 성공했습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> modelInit(@AuthenticationPrincipal Long userId,
								@Schema(description = "Model ID", example = "1")
								@PathVariable Long modelId, @RequestBody PipelineModelRequest.InitDto requestDto) throws IOException, ParseException;

	@GetMapping
	@Operation(summary = "파이프라인 모델 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파이프라인 모델 리스트 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"모델 리스트 조회에 성공했습니다.\"}," +
							"\"body\": {\n" +
							"    \"buildings\": [\n" +
							"      {\n" +
							"        \"building\": \"역삼 멀티캠퍼스\",\n" +
							"        \"floors\": [\n" +
							"          {\n" +
							"            \"floor\": 1,\n" +
							"            \"models\": [\n" +
							"              {\n" +
							"                \"modelId\": 1,\n" +
							"                \"name\": \"model1\",\n" +
							"                \"previewUrl\": \"previewUrl1\",\n" +
							"                \"updatedAt\": \"2024-11-03 00:14:34\"\n" +
							"              },\n" +
							"              {\n" +
							"                \"modelId\": 2,\n" +
							"                \"name\": \"model2\",\n" +
							"                \"previewUrl\": \"previewUrl2\",\n" +
							"                \"updatedAt\": \"2024-11-03 00:14:34\"\n" +
							"              }\n" +
							"            ]\n" +
							"          },\n" +
							"          {\n" +
							"            \"floor\": 2,\n" +
							"            \"models\": [\n" +
							"              {\n" +
							"                \"modelId\": 3,\n" +
							"                \"name\": \"model3\",\n" +
							"                \"previewUrl\": \"previewUrl3\",\n" +
							"                \"updatedAt\": \"2024-11-03 00:14:34\"\n" +
							"              }\n" +
							"            ]\n" +
							"          }\n" +
							"        ]\n" +
							"      }\n" +
							"    ]\n" +
							"  }}")}
			))
	})
	ResponseEntity<?> modelList(@AuthenticationPrincipal Long userId, @RequestParam(required = false) String building, @RequestParam(required = false) Integer floor);

	@GetMapping("/{modelId}")
	@Operation(summary = "파이프라인 모델 상세조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파이프라인 모델 상세조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"모델 상세 조회에 성공했습니다.\"}," +
							"\"body\": {\n" +
							"    \"name\": \"Pipeline Model\",\n" +
							"    \"modelingUrl\": \"https://pipewatch-bucket.s3.ap-northeast-2.amazonaws.com/pipeline/model/pipeline_8b6ab7d3-aa36-4638-898f-36b25024ac24.gltf\",\n" +
							"    \"isCompleted\": true,\n" +
							"    \"updatedAt\": \"2024-11-05 11:35:38\",\n" +
							"    \"pipelines\": [\n" +
							"      {\n" +
							"        \"pipelineUuid\": \"PipeObj_2_d4562232-7715-45f6-bddb-588f4ca7a253\",\n" +
							"        \"pipeUuids\": [\n" +
							"          \"PipeObj_2_Segment_1_d4562232-7715-45f6-bddb-588f4ca7a253\",\n" +
							"          \"PipeObj_2_Segment_2_d4562232-7715-45f6-bddb-588f4ca7a253\",\n" +
							"          \"PipeObj_2_Segment_3_d4562232-7715-45f6-bddb-588f4ca7a253\"\n" +
							"        ]\n" +
							"      },\n" +
							"      {\n" +
							"        \"pipelineUuid\": \"PipeObj_1_d4562232-7715-45f6-bddb-588f4ca7a253\",\n" +
							"        \"pipeUuids\": [\n" +
							"          \"PipeObj_1_Segment_1_d4562232-7715-45f6-bddb-588f4ca7a253\",\n" +
							"          \"PipeObj_1_Segment_2_d4562232-7715-45f6-bddb-588f4ca7a253\",\n" +
							"          \"PipeObj_1_Segment_3_d4562232-7715-45f6-bddb-588f4ca7a253\",\n" +
							"          \"PipeObj_1_Segment_4_d4562232-7715-45f6-bddb-588f4ca7a253\",\n" +
							"          \"PipeObj_1_Segment_5_d4562232-7715-45f6-bddb-588f4ca7a253\",\n" +
							"          \"PipeObj_1_Segment_6_d4562232-7715-45f6-bddb-588f4ca7a253\"\n" +
							"        ]\n" +
							"      },\n" +
							"      {\n" +
							"        \"pipelineUuid\": \"PipeObj_3_d4562232-7715-45f6-bddb-588f4ca7a253\",\n" +
							"        \"pipeUuids\": [\n" +
							"          \"PipeObj_3_Segment_1_d4562232-7715-45f6-bddb-588f4ca7a253\",\n" +
							"          \"PipeObj_3_Segment_2_d4562232-7715-45f6-bddb-588f4ca7a253\"\n" +
							"        ]\n" +
							"      }\n" +
							"    ],\n" +
							"    \"memos\": [\n" +
							"      {\n" +
							"        \"memoId\": 2,\n" +
							"        \"memo\": \"hihihi\",\n" +
							"        \"writer\": {\n" +
							"          \"userUuid\": \"8e7dfbe3-aeca-4392-8d90-c1d3ae4fd35f\",\n" +
							"          \"userName\": \"파오리\"\n" +
							"        },\n" +
							"        \"createdAt\": \"2024-11-03 14:52:57\"\n" +
							"      },\n" +
							"      {\n" +
							"        \"memoId\": 1,\n" +
							"        \"memo\": \"hi\",\n" +
							"        \"writer\": {\n" +
							"          \"userUuid\": \"8e7dfbe3-aeca-4392-8d90-c1d3ae4fd35f\",\n" +
							"          \"userName\": \"파오리\"\n" +
							"        },\n" +
							"        \"createdAt\": \"2024-11-05 14:52:55\"\n" +
							"      }\n" +
							"    ],\n" +
							"    \"creator\": {\n" +
							"      \"userUuid\": \"8e7dfbe3-aeca-4392-8d90-c1d3ae4fd35f\",\n" +
							"      \"userName\": \"파오리\"\n" +
							"    }\n" +
							"  }}")}
			))
	})
	ResponseEntity<?> modelDetail(@AuthenticationPrincipal Long userId,
								  @Schema(description = "Model ID", example = "1")
								  @PathVariable Long modelId);

	@PatchMapping("/{modelId}")
	@Operation(summary = "파이프라인 모델 정보 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파이프라인 모델 정보 수정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"모델 정보 수정에 성공했습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> modelModify(
			@AuthenticationPrincipal Long userId,
			@Schema(description = "Model ID", example = "1")
			@PathVariable Long modelId, @RequestBody PipelineModelRequest.ModifyDto requestDto);

	@DeleteMapping("/{modelId}")
	@Operation(summary = "파이프라인 모델 삭제")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "파이프라인 모델 삭제 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 204, \"message\": \"모델 삭제에 성공했습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> modelDelete(@AuthenticationPrincipal Long userId,
								  @Schema(description = "Model ID", example = "1")
								  @PathVariable Long modelId);
}
