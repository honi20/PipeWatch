package com.pipewatch.domain.management.controller;

import com.pipewatch.domain.management.model.dto.ManagementRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Management API", description = "Management API Document")
public interface ManagementApiSwagger {
	@GetMapping("/waiting-list")
	@Operation(summary = "승인 대기 리스트 조회", description = "기업에 승인 요청 중인 유저 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "승인 대기 리스트 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"승인 대기 중인 직원 목록 조회에 성공했습니다.\"}," +
							"\"body\": {" +
							"    \"employees\": [" +
							"      {" +
							"        \"uuid\": \"1604b772-adc0-4212-8a90-81186c57f598\"," +
							"        \"name\": \"테스트\"," +
							"        \"email\": \"test@ssafy.com\"," +
							"        \"empNo\": 1243242," +
							"        \"department\": \"IT사업부\"," +
							"        \"empClass\": \"팀장\"," +
							"        \"role\": \"USER\"" +
							"      }" +
							"    ]" +
							"  }}")}
			)),
			@ApiResponse(responseCode = "403", description = "승인 대기 리스트 조회 실패 - 기업 유저만 조회 가능", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 403, \"message\": \"접근 권한이 없는 유저입니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> waitingEmployeeList(@AuthenticationPrincipal Long userId);

	@GetMapping
	@Operation(summary = "기업 직원 리스트 조회", description = "해당 기업에 속한 직원(사원/관리자) 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "기업 직원 리스트 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"직원 목록 조회에 성공했습니다.\"}," +
							"\"body\": {" +
							"    \"employees\": [" +
							"      {" +
							"        \"uuid\": \"1604b772-adc0-4212-8a90-81186c57f600\"," +
							"        \"name\": \"최싸피\"," +
							"        \"email\": \"choi@ssafy.com\"," +
							"        \"empNo\": 1534534," +
							"        \"department\": \"마케팅부\"," +
							"        \"empClass\": \"대리\"," +
							"        \"role\": \"EMPLOYEE\"" +
							"      }," +
							"      {" +
							"        \"uuid\": \"1604b772-adc0-4212-8a90-81186c57f601\"," +
							"        \"name\": \"김싸피\"," +
							"        \"email\": \"kim@ssafy.com\"," +
							"        \"empNo\": 1423435," +
							"        \"department\": \"인사부\"," +
							"        \"empClass\": \"부장\"," +
							"        \"role\": \"ADMIN\"" +
							"      }" +
							"    ]" +
							"  }}")}
			))
	})
	ResponseEntity<?> employeeList(@AuthenticationPrincipal Long userId);

	@PatchMapping
	@Operation(summary = "접근 권한 수정", description = "직원의 권한(EMPLOYEE/ADMIN) 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "접근 권한 수정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"권한 변경에 성공했습니다.\"}, \"body\": null}")}
			)),
			@ApiResponse(responseCode = "403", description = "접근 권한 수정 실패 - 기업 유저만 수정 가능", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 403, \"message\": \"접근 권한이 없는 유저입니다.\"},\n\"body\": null}")}
			)),
			@ApiResponse(responseCode = "404", description = "접근 권한 수정 실패 - 존재하지 않는 Role", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 404, \"message\": \"해당 Role이 존재하지 않습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> userRollModify(@AuthenticationPrincipal Long userId, @RequestBody ManagementRequest.AccessModifyDto requestDto);

	@GetMapping("/search")
	@Operation(summary = "직원 검색", description = "이름/이메일을 기준으로 기업 직원 검색")
	@Parameter(name = "keyword", description = "검색어 (이름/이메일)", example = "ssafy")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "직원 검색 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"사원 검색에 성공했습니다.\"}," +
							"\"body\": {" +
							"    \"employees\": [" +
							"      {" +
							"        \"uuid\": \"1604b772-adc0-4212-8a90-81186c57f600\"," +
							"        \"name\": \"최싸피\"," +
							"        \"email\": \"choi@ssafy.com\"," +
							"        \"empNo\": 1534534," +
							"        \"department\": \"마케팅부\"," +
							"        \"empClass\": \"대리\"," +
							"        \"role\": \"EMPLOYEE\"" +
							"      }," +
							"      {" +
							"        \"uuid\": \"1604b772-adc0-4212-8a90-81186c57f601\"," +
							"        \"name\": \"김싸피\"," +
							"        \"email\": \"kim@ssafy.com\"," +
							"        \"empNo\": 1423435," +
							"        \"department\": \"인사부\"," +
							"        \"empClass\": \"부장\"," +
							"        \"role\": \"ADMIN\"" +
							"      }" +
							"    ]" +
							"  }}")}
			))
	})
	ResponseEntity<?> employeeDetail(@AuthenticationPrincipal Long userId, @RequestParam(required = false) String keyword);
}
