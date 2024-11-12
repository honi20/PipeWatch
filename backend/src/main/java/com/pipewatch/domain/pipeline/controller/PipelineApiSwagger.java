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
	@Operation(summary = "단일 파이프라인 상세조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "단일 파이프라인 상세조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"단일 파이프라인 조회에 성공했습니다.\"}," +
							"\"body\": {\n" +
							"    \"name\": \"PipeLine_1\",\n" +
							"    \"property\": {\n" +
							"      \"pipeMaterial\": {\n" +
							"        \"materialId\": 1,\n" +
							"        \"koreanName\": \"알루미늄\",\n" +
							"        \"englishName\": \"Aluminum\"\n" +
							"      },\n" +
							"      \"outerDiameter\": 150,\n" +
							"      \"innerDiameter\": 10.3,\n" +
							"      \"fluidMaterial\": {\n" +
							"        \"materialId\": 4,\n" +
							"        \"koreanName\": \"수증기\",\n" +
							"        \"englishName\": \"Steam\"\n" +
							"      },\n" +
							"      \"velocity\": 1.5\n" +
							"    },\n" +
							"    \"updatedAt\": \"2024-11-11 12:59:14\"\n" +
							"  }}")}
			))
	})
	ResponseEntity<?> pipelineDetail(@AuthenticationPrincipal Long userId,
									 @Schema(description = "파이프라인 Id", example = "1")
									 @PathVariable Long pipelineId);

	@PutMapping("/{pipelineId}")
	@Operation(summary = "단일 파이프라인 기본 정보 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "단일 파이프라인 기본 정보 수정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"단일 파이프라인 기본 정보 수정에 성공했습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> pipelineModify(@AuthenticationPrincipal Long userId,
									 @Schema(description = "파이프라인 Id", example = "1")
									 @PathVariable Long pipelineId, @RequestBody PipelineRequest.ModifyDto requestDto);

	@GetMapping("/property")
	@Operation(summary = "파이프 속성 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파이프 속성 리스트 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"단일 파이프라인 속성 리스트 조회에 성공했습니다.\"}," +
							"\"body\": [\n" +
							"    {\n" +
							"      \"type\": \"PIPE\",\n" +
							"      \"materials\": [\n" +
							"        {\n" +
							"          \"materialId\": 1,\n" +
							"          \"koreanName\": \"알루미늄\",\n" +
							"          \"englishName\": \"Aluminum\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"materialId\": 2,\n" +
							"          \"koreanName\": \"철\",\n" +
							"          \"englishName\": \"Steel\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"materialId\": 3,\n" +
							"          \"koreanName\": \"스테인리스\",\n" +
							"          \"englishName\": \"Stainless Steel\"\n" +
							"        }\n" +
							"      ]\n" +
							"    },\n" +
							"    {\n" +
							"      \"type\": \"FLUID\",\n" +
							"      \"materials\": [\n" +
							"        {\n" +
							"          \"materialId\": 4,\n" +
							"          \"koreanName\": \"수증기\",\n" +
							"          \"englishName\": \"Steam\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"materialId\": 5,\n" +
							"          \"koreanName\": \"공기\",\n" +
							"          \"englishName\": \"Air\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"materialId\": 6,\n" +
							"          \"koreanName\": \"물\",\n" +
							"          \"englishName\": \"Water\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"materialId\": 7,\n" +
							"          \"koreanName\": \"기름\",\n" +
							"          \"englishName\": \"Oil\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"materialId\": 8,\n" +
							"          \"koreanName\": \"증기\",\n" +
							"          \"englishName\": \"Vapor\"\n" +
							"        }\n" +
							"      ]\n" +
							"    }\n" +
							"  ]}")}
			))
	})
	ResponseEntity<?> pipeMaterialList();

	@PutMapping("/{pipelineId}/property")
	@Operation(summary = "단일 파이프라인 속성 정보 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "단일 파이프라인 속성 정보 수정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"단일 파이프라인 속성 정보 수정에 성공했습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> pipelinePropertyModify(@AuthenticationPrincipal Long userId,
											 @Schema(description = "파이프라인 Id", example = "1")
											 @PathVariable Long pipelineId, @RequestBody PipelineRequest.ModifyPropertyDto requestDto);

	@PostMapping("/pipes/{pipeId}")
	@Operation(summary = "객체 파이프 메모 생성")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "객체 파이프 메모 생성 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 201, \"message\": \"파이프 메모가 생성되었습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> pipeMemoCreate(@AuthenticationPrincipal Long userId,
									 @Schema(description = "파이프 ID", example = "1")
									 @PathVariable Long pipeId, @RequestBody PipelineRequest.CreateMemoDto requestDto);

	@GetMapping("/{pipelineId}")
	@Operation(summary = "단일 파이프라인 메모 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "단일 파이프 메모 리스트 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"파이프 메모 리스트 조회에 성공했습니다.\"}," +
							"\"body\": {\n" +
							"    \"totalMemoList\": [\n" +
							"      {\n" +
							"        \"pipeId\": 13,\n" +
							"        \"pipeName\": \"Pipe_1\",\n" +
							"        \"memoList\": [\n" +
							"          {\n" +
							"            \"memoId\": 1,\n" +
							"            \"memo\": \"파이프에 결함 존재\",\n" +
							"            \"writer\": {\n" +
							"              \"userUuid\": \"ssafy12\",\n" +
							"              \"userName\": \"김싸피\"\n" +
							"            },\n" +
							"            \"createdAt\": \"2024-11-06 15:50:23\"\n" +
							"          },\n" +
							"          {\n" +
							"            \"memoId\": 4,\n" +
							"            \"memo\": \"파이프1 확인 완료\",\n" +
							"            \"writer\": {\n" +
							"              \"userUuid\": \"ssafy12\",\n" +
							"              \"userName\": \"김싸피\"\n" +
							"            },\n" +
							"            \"createdAt\": \"2024-11-06 16:07:02\"\n" +
							"          }\n" +
							"        ]\n" +
							"      },\n" +
							"      {\n" +
							"        \"pipeId\": 14,\n" +
							"        \"pipeName\": \"Pipe_2\",\n" +
							"        \"memoList\": [\n" +
							"          {\n" +
							"            \"memoId\": 2,\n" +
							"            \"memo\": \"파이프2에 결함 존재\",\n" +
							"            \"writer\": {\n" +
							"              \"userUuid\": \"ssafy12\",\n" +
							"              \"userName\": \"김싸피\"\n" +
							"            },\n" +
							"            \"createdAt\": \"2024-11-06 15:50:33\"\n" +
							"          }\n" +
							"        ]\n" +
							"      },\n" +
							"      {\n" +
							"        \"pipeId\": 16,\n" +
							"        \"pipeName\": \"Pipe_4\",\n" +
							"        \"memoList\": [\n" +
							"          {\n" +
							"            \"memoId\": 3,\n" +
							"            \"memo\": \"파이프4 확인 완료\",\n" +
							"            \"writer\": {\n" +
							"              \"userUuid\": \"ssafy12\",\n" +
							"              \"userName\": \"김싸피\"\n" +
							"            },\n" +
							"            \"createdAt\": \"2024-11-06 15:50:51\"\n" +
							"          }\n" +
							"        ]\n" +
							"      }\n" +
							"    ]\n" +
							"  }}")}
			))
	})
	ResponseEntity<?> pipelineMemoList(@AuthenticationPrincipal Long userId,
									   @Schema(description = "파이프라인 Id", example = "1")
									   @PathVariable Long pipelineId);


	@GetMapping("/pipes/{pipeId}")
	@Operation(summary = "객체 파이프 메모 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "객체 파이프 메모 리스트 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"파이프 메모 리스트 조회에 성공했습니다.\"}," +
							"\"body\": {\n" +
							"    \"pipeId\": 13,\n" +
							"    \"pipeName\": \"Pipe_1\",\n" +
							"    \"memoList\": [\n" +
							"      {\n" +
							"        \"memoId\": 1,\n" +
							"        \"memo\": \"파이프에 결함 존재\",\n" +
							"        \"writer\": {\n" +
							"          \"userUuid\": \"ssafy12\",\n" +
							"          \"userName\": \"김싸피\"\n" +
							"        },\n" +
							"        \"createdAt\": \"2024-11-06 15:50:23\"\n" +
							"      },\n" +
							"      {\n" +
							"        \"memoId\": 4,\n" +
							"        \"memo\": \"파이프1 확인 완료\",\n" +
							"        \"writer\": {\n" +
							"          \"userUuid\": \"ssafy12\",\n" +
							"          \"userName\": \"김싸피\"\n" +
							"        },\n" +
							"        \"createdAt\": \"2024-11-06 16:07:02\"\n" +
							"      }\n" +
							"    ]\n" +
							"  }}")}
			))
	})
	ResponseEntity<?> pipeMemoList(@AuthenticationPrincipal Long userId,
								   @Schema(description = "파이프 Id", example = "1")
								   @PathVariable Long pipeId);

	@DeleteMapping("/pipes/{memoId}")
	@Operation(summary = "객체 파이프 메모 삭제")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "객체 파이프 메모 삭제 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 204, \"message\": \"메모 삭제에 성공했습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> pipeMemoDelete(@AuthenticationPrincipal Long userId,
									 @Schema(description = "메모 ID", example = "1")
									 @PathVariable Long memoId);
}
