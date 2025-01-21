package com.pipewatch.domain.enterprise.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Enterprise API", description = "Enterprise API Document")
public interface EnterpriseApiSwagger {
	@GetMapping("/detail")
	@Operation(summary = "기업 상세 조회", description = "유저가 속한 기업 정보 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "기업 상세 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"기업 정보 조회에 성공했습니다.\"}," +
							"\"body\": {" +
							"    \"name\": \"paori\",\n" +
							"    \"industry\": \"제조업\",\n" +
							"    \"managerEmail\": \"pipewatch@paori.com\",\n" +
							"    \"managerPhoneNumber\": \"010-1234-5678\"\n" +
							"  }}")}
			))
	})
	ResponseEntity<?> enterpriseDetail(@AuthenticationPrincipal Long userId);

	@GetMapping
	@Operation(summary = "기업 리스트 조회", description = "Pipe Watch에 속한 기업 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "기업 리스트 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"기업 리스트 조회에 성공했습니다.\"}," +
							"\"body\": {\n" +
							"    \"enterprises\": [\n" +
							"      {\n" +
							"        \"enterpriseId\": 1,\n" +
							"        \"name\": \"paori\",\n" +
							"        \"industry\": \"제조업\"\n" +
							"      },\n" +
							"      {\n" +
							"        \"enterpriseId\": 2,\n" +
							"        \"name\": \"samsung\",\n" +
							"        \"industry\": \"제조업\"\n" +
							"      }\n" +
							"    ]\n" +
							"  }}")}
			))
	})
	ResponseEntity<?> enterpriseList();

	@GetMapping("/buildings")
	@Operation(summary = "건물 리스트 조회", description = "유저가 속한 기업의 건물 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "건물 리스트 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"건물 리스트 조회에 성공했습니다.\"}," +
							"\"body\": {\n" +
							"    \"buildings\": [\"역삼 멀티캠퍼스\", \"부울경 멀티캠퍼스\"]" +
							" }}")}
			))
	})
	ResponseEntity<?> buildingList(@AuthenticationPrincipal Long userId);

	@GetMapping("/buildings/floors")
	@Operation(summary = "건물 및 층수 조회", description = "유저가 속한 기업의 건물 및 층수 리스트 조회")
	@ApiResponse(responseCode = "200", description = "건물 및 층수 리스트 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
			examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"건물 및 층수 리스트 조회에 성공했습니다.\"}," +
					"\"body\": {\n" +
					"    \"buildings\": [\n" +
					"      {\n" +
					"        \"building\": \"역삼 멀티캠퍼스\",\n" +
					"        \"floors\": [\n" +
					"          12,\n" +
					"          14\n" +
					"        ]\n" +
					"      },\n" +
					"      {\n" +
					"        \"building\": \"부울경 멀티캠퍼스\",\n" +
					"        \"floors\": [\n" +
					"          1,\n" +
					"          3\n" +
					"        ]\n" +
					"      }\n" +
					"    ]\n" +
					"  }}")}
	))
	ResponseEntity<?> buildingAndFloorList(@AuthenticationPrincipal Long userId);
}
