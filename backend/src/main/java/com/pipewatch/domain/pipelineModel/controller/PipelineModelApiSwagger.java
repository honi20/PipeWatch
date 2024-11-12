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
	@PostMapping(value = "/upload-img", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@Operation(summary = "Image File 업로드")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Image File 업로드 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 201, \"message\": \"이미지 파일 업로드에 성공했습니다. 파이프라인 모델이 생성될 예정입니다.\"},\n\"body\": {\"modelId\": 1}}")}
			)),
			@ApiResponse(responseCode = "400", description = "Image File 업로드 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {
							@ExampleObject(name = "빈 파일", value = "{\"header\":{\"httpStatusCode\": 400, \"message\": \"파일 업로드에 실패하였습니다.\"},\n\"body\": null}"),
							@ExampleObject(name = "적절하지 않은 파일 확장자", value = "{\"header\":{\"httpStatusCode\": 400, \"message\": \"적절하지 않은 파일 확장자입니다.\"},\n\"body\": null}"),
							@ExampleObject(name = "파이프가 존재하지 않는 이미지", value = "{\"header\":{\"httpStatusCode\": 400, \"message\": \"적절하지 않은 이미지 파일 형식입니다. 이미지에서 발견된 파이프가 존재하지 않습니다.\"},\n\"body\": null}")
					}
			)),
			@ApiResponse(responseCode = "403", description = "Image File 업로드 실패 - 기업 및 관리자 유저만 생성 가능", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 403, \"message\": \"접근 권한이 없는 유저입니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> imageUpload(@AuthenticationPrincipal Long userId, @RequestPart(value = "file") MultipartFile file) throws IOException, ParseException;

	@PostMapping(value = "/upload-file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@Operation(summary = "GLTF File 업로드")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "GLTF File 업로드 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 201, \"message\": \"GLTF 파일 업로드에 성공했습니다. 파이프라인 모델이 생성되었습니다.\"},\n\"body\": {\"modelId\": 1}}")}
			)),
			@ApiResponse(responseCode = "400", description = "GLTF File 업로드 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {
							@ExampleObject(name = "빈 파일", value = "{\"header\":{\"httpStatusCode\": 400, \"message\": \"파일 업로드에 실패하였습니다.\"},\n\"body\": null}"),
							@ExampleObject(name = "적절하지 않은 파일 확장자", value = "{\"header\":{\"httpStatusCode\": 400, \"message\": \"적절하지 않은 파일 확장자입니다.\"},\n\"body\": null}"),
							@ExampleObject(name = "파이프 객체 name이 존재하지 않는 파일", value = "{\"header\":{\"httpStatusCode\": 400, \"message\": \"적절하지 않은 파일 형식입니다. meshes 리스트에 각 객체의 name이 존재해야 합니다.\"},\n\"body\": null}")
					}
			)),
			@ApiResponse(responseCode = "403", description = "GLTF File 업로드 실패 - 기업 및 관리자 유저만 생성 가능", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 403, \"message\": \"접근 권한이 없는 유저입니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> fileUpload(@AuthenticationPrincipal Long userId, @RequestPart(value = "file") MultipartFile file) throws IOException, ParseException;

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
							"    \"models\": [\n" +
							"      {\n" +
							"        \"modelId\": 4,\n" +
							"        \"name\": \"Pipeline Model\",\n" +
							"        \"previewUrl\": \"https://pipewatch-bucket.s3.ap-northeast-2.amazonaws.com/assets/no_thumbnail.png\",\n" +
							"        \"building\": \"역삼 멀티캠퍼스\",\n" +
							"        \"floor\": 14,\n" +
							"        \"updatedAt\": \"2024-11-11 12:22:19\"\n" +
							"      },\n" +
							"      {\n" +
							"        \"modelId\": 5,\n" +
							"        \"name\": \"Pipeline Model\",\n" +
							"        \"previewUrl\": \"https://pipewatch-bucket.s3.ap-northeast-2.amazonaws.com/assets/no_thumbnail.png\",\n" +
							"        \"building\": null,\n" +
							"        \"floor\": null,\n" +
							"        \"updatedAt\": \"2024-11-11 12:34:39\"\n" +
							"      },\n" +
							"      {\n" +
							"        \"modelId\": 6,\n" +
							"        \"name\": \"Pipeline Model\",\n" +
							"        \"previewUrl\": \"https://pipewatch-bucket.s3.ap-northeast-2.amazonaws.com/assets/no_thumbnail.png\",\n" +
							"        \"building\": null,\n" +
							"        \"floor\": null,\n" +
							"        \"updatedAt\": \"2024-11-11 12:53:50\"\n" +
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
							"    \"modelingUrl\": \"https://pipewatch-bucket.s3.ap-northeast-2.amazonaws.com/models/PipeLine_7d521f29-814d-4077-b3a6-04a425bd64bf.gltf\",\n" +
							"    \"building\": \"역삼 멀티캠퍼스\",\n" +
							"    \"floor\": 11,\n" +
							"    \"isCompleted\": true,\n" +
							"    \"updatedAt\": \"2024-11-06 13:20:53\",\n" +
							"    \"pipelines\": [\n" +
							"      {\n" +
							"        \"pipelineId\": 25,\n" +
							"        \"pipes\": [\n" +
							"          {\n" +
							"            \"pipeId\": 96,\n" +
							"            \"pipeUuid\": \"PipeObj_1_Segment_1\"\n" +
							"          },\n" +
							"          {\n" +
							"            \"pipeId\": 97,\n" +
							"            \"pipeUuid\": \"PipeObj_1_Segment_2\"\n" +
							"          },\n" +
							"          {\n" +
							"            \"pipeId\": 98,\n" +
							"            \"pipeUuid\": \"PipeObj_1_Segment_3\"\n" +
							"          },\n" +
							"          {\n" +
							"            \"pipeId\": 99,\n" +
							"            \"pipeUuid\": \"PipeObj_1_Segment_4\"\n" +
							"          },\n" +
							"          {\n" +
							"            \"pipeId\": 100,\n" +
							"            \"pipeUuid\": \"PipeObj_1_Segment_5\"\n" +
							"          },\n" +
							"          {\n" +
							"            \"pipeId\": 101,\n" +
							"            \"pipeUuid\": \"PipeObj_1_Segment_6\"\n" +
							"          }\n" +
							"        ]\n" +
							"      },\n" +
							"      {\n" +
							"        \"pipelineId\": 26,\n" +
							"        \"pipes\": [\n" +
							"          {\n" +
							"            \"pipeId\": 102,\n" +
							"            \"pipeUuid\": \"PipeObj_2_Segment_1\"\n" +
							"          },\n" +
							"          {\n" +
							"            \"pipeId\": 103,\n" +
							"            \"pipeUuid\": \"PipeObj_2_Connector_1\"\n" +
							"          },\n" +
							"          {\n" +
							"            \"pipeId\": 104,\n" +
							"            \"pipeUuid\": \"PipeObj_2_Segment_2\"\n" +
							"          },\n" +
							"          {\n" +
							"            \"pipeId\": 105,\n" +
							"            \"pipeUuid\": \"PipeObj_2_Segment_3\"\n" +
							"          }\n" +
							"        ]\n" +
							"      },\n" +
							"      {\n" +
							"        \"pipelineId\": 27,\n" +
							"        \"pipes\": [\n" +
							"          {\n" +
							"            \"pipeId\": 106,\n" +
							"            \"pipeUuid\": \"PipeObj_3_Segment_1\"\n" +
							"          },\n" +
							"          {\n" +
							"            \"pipeId\": 107,\n" +
							"            \"pipeUuid\": \"PipeObj_3_Segment_2\"\n" +
							"          }\n" +
							"        ]\n" +
							"      }\n" +
							"    ],\n" +
							"    \"creator\": {\n" +
							"      \"userUuid\": \"ssafy12\",\n" +
							"      \"userName\": \"김싸피\"\n" +
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

	@PatchMapping("/thumbnail/{modelId}")
	@Operation(summary = "파이프라인 모델 썸네일 이미지 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파이프라인 모델 썸네일 이미지 수정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"모델 정보 수정에 성공했습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> modelThumbnailModify(@AuthenticationPrincipal Long userId,
										   @PathVariable Long modelId,
										   @RequestPart(value = "file") MultipartFile file) throws IOException;

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


	@GetMapping("/memos/{modelId}")
	@Operation(summary = "파이프라인 모델 메모 리스트")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파이프라인 모델 메모 리스트 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"모델 메모 리스트 조회에 성공했습니다.\"}," +
							"\"body\": {\n" +
							"    \"memoList\": [\n" +
							"      {\n" +
							"        \"memoId\": 1,\n" +
							"        \"memo\": \"hi\",\n" +
							"        \"writer\": {\n" +
							"          \"userUuid\": \"ssafy12\",\n" +
							"          \"userName\": \"김싸피\"\n" +
							"        },\n" +
							"        \"createdAt\": \"2024-11-05 17:32:19\"\n" +
							"      },\n" +
							"      {\n" +
							"        \"memoId\": 3,\n" +
							"        \"memo\": \"hihihi\",\n" +
							"        \"writer\": {\n" +
							"          \"userUuid\": \"ssafy12\",\n" +
							"          \"userName\": \"김싸피\"\n" +
							"        },\n" +
							"        \"createdAt\": \"2024-11-05 17:52:42\"\n" +
							"      }\n" +
							"    ]\n" +
							"  }}")}
			))
	})
	ResponseEntity<?> modelMemoList(@AuthenticationPrincipal Long userId, @PathVariable Long modelId);

	@PostMapping("/memos/{modelId}")
	@Operation(summary = "파이프라인 모델 메모 생성")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "파이프라인 모델 메모 생성 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 201, \"message\": \"모델 메모 생성에 성공했습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> modelMemoCreate(@AuthenticationPrincipal Long userId, @PathVariable Long modelId, @RequestBody PipelineModelRequest.MemoDto requestDto);

	@DeleteMapping("/memos/{memoId}")
	@Operation(summary = "파이프라인 모델 메모 삭제")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "파이프라인 모델 메모 삭제 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 204, \"message\": \"모델 메모 삭제에 성공했습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> modelMemoCreate(@AuthenticationPrincipal Long userId, @PathVariable Long memoId);
}
