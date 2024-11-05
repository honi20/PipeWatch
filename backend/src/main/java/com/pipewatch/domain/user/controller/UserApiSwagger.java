package com.pipewatch.domain.user.controller;

import com.pipewatch.domain.user.model.dto.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "User API Document")
public interface UserApiSwagger {
	@GetMapping("/mypage")
	@Operation(summary = "마이페이지 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "마이페이지 조회 성공", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							examples = {
									@ExampleObject(name = "직원 마이페이지 조회", value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"개인정보 조회에 성공했습니다.\"}," +
											"\"body\": {\n" +
											"    \"name\": \"최싸피\",\n" +
											"    \"email\": \"test@ssafy.com\",\n" +
											"    \"role\": \"EMPLOYEE\",\n" +
											"    \"state\": \"PENDING\",\n" +
											"    \"enterpriseName\": \"paori\",\n" +
											"    \"employee\": {\n" +
											"      \"empNo\": 1243242,\n" +
											"      \"department\": \"IT사업부\",\n" +
											"      \"empClass\": \"팀장\"\n" +
											"    }\n" +
											"  }}"),
									@ExampleObject(name = "기업 마이페이지 조회", value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"개인정보 조회에 성공했습니다.\"}," +
											"\"body\": {\n" +
											"    \"name\": \"paori\",\n" +
											"    \"email\": \"pipewatch_admin@ssafy.com\",\n" +
											"    \"role\": \"ENTERPRISE\",\n" +
											"    \"state\": \"ACTIVE\",\n" +
											"    \"enterpriseName\": \"paori\",\n" +
											"    \"employee\": null\n" +
											"  }}"),
									@ExampleObject(name = "일반유저 마이페이지 조회", value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"개인정보 조회에 성공했습니다.\"}," +
											"\"body\": {\n" +
											"    \"name\": \"파오리\",\n" +
											"    \"email\": \"1594cyh@gmail.com\",\n" +
											"    \"role\": \"USER\",\n" +
											"    \"state\": \"PENDING\",\n" +
											"    \"enterpriseName\": null,\n" +
											"    \"employee\": null\n" +
											"  }}")
							})
			})
	})
	ResponseEntity<?> myPage(@AuthenticationPrincipal Long userId);

	@PutMapping("/mypage")
	@Operation(summary = "개인정보 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "개인정보 수정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"개인정보 수정에 성공했습니다.\"},\n\"body\": null}")}
			)),
			@ApiResponse(responseCode = "403", description = "개인정보 수정 실패 - 일반 및 기업 유저는 수정 불가능", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 403, \"message\": \"접근 권한이 없는 유저입니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> mypageModify(@AuthenticationPrincipal Long userId, @RequestBody UserRequest.MyPageModifyDto requestDto);

	@PatchMapping("/modify-pwd")
	@Operation(summary = "비밀번호 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "비밀번호 수정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"비밀번호 수정에 성공했습니다.\"},\n\"body\": null}")}
			)),
			@ApiResponse(responseCode = "403", description = "비밀번호 수정 실패 - 잘못된 비밀번호", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 403, \"message\": \"비밀번호가 일치하지 않습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> passwordModify(@AuthenticationPrincipal Long userId, @RequestBody UserRequest.PasswordModifyDto requestDto);

	@DeleteMapping("/withdraw")
	@Operation(summary = "회원 탈퇴")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "회원 탈퇴 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 204, \"message\": \"회원 탈퇴에 성공했습니다.\"},\n\"body\": null}")}
			)),
			@ApiResponse(responseCode = "403", description = "회원 탈퇴 실패 - 잘못된 비밀번호", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 403, \"message\": \"비밀번호가 일치하지 않습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> withdraw(@AuthenticationPrincipal Long userId, @RequestBody UserRequest.WithdrawDto requestDto);
}
