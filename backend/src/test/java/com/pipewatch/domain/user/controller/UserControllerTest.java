package com.pipewatch.domain.user.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipewatch.domain.user.model.dto.UserRequest;
import com.pipewatch.domain.user.model.dto.UserResponse;
import com.pipewatch.domain.user.service.UserService;
import com.pipewatch.global.exception.BaseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.pipewatch.domain.util.ResponseFieldUtil.getCommonResponseFields;
import static com.pipewatch.global.statusCode.ErrorCode.INVALID_PASSWORD;
import static com.pipewatch.global.statusCode.SuccessCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@DisplayName("User API 명세서")
@WithMockUser
@ActiveProfiles("test")
class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@Test
	void 직원_마이페이지_조회_성공() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(123L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		UserResponse.MyPageDto response = UserResponse.MyPageDto.builder()
				.name("최싸피")
				.email("test@ssafy.com")
				.enterpriseName("paori")
				.role("ROLE_EMPLOYEE")
				.state("PENDING")
				.employee(new UserResponse.EmployeeDto(1243242L, "IT사업부","팀장"))
				.build();

		when(userService.getUserDetail(123L)).thenReturn(response);

		ResultActions actions = mockMvc.perform(
				get("/api/users/mypage")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(MYPAGE_DETAIL_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(MYPAGE_DETAIL_OK.getMessage()))
				.andDo(document(
						"직원 마이페이지 조회 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("User API")
								.summary("마이페이지 조회 API")
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body.name").type(JsonFieldType.STRING).description("이름"),
												fieldWithPath("body.email").type(JsonFieldType.STRING).description("이메일"),
												fieldWithPath("body.enterpriseName").type(JsonFieldType.STRING).description("기업명"),
												fieldWithPath("body.role").type(JsonFieldType.STRING).description("역할 (사원/관리자/기업)"),
												fieldWithPath("body.state").type(JsonFieldType.STRING).description("상태 여부 (pending/active/inactive/rejected)"),
												fieldWithPath("body.employee").type(JsonFieldType.OBJECT).description("직원 정보"),
												fieldWithPath("body.employee.empNo").type(JsonFieldType.NUMBER).description("사번"),
												fieldWithPath("body.employee.department").type(JsonFieldType.STRING).description("부서"),
												fieldWithPath("body.employee.empClass").type(JsonFieldType.STRING).description("직급")
										)
								)
								.responseSchema(Schema.schema("직원 마이페이지 조회 Response"))
								.build()
						)));
	}

	@Test
	void 기업_마이페이지_조회_성공() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(124L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		UserResponse.MyPageDto response = UserResponse.MyPageDto.builder()
				.name("paori")
				.email("pipewatch_admin@ssafy.com")
				.enterpriseName("paori")
				.role("ROLE_ENTERPRISE")
				.state("ACTIVE")
				.employee(null)
				.build();

		when(userService.getUserDetail(124L)).thenReturn(response);

		ResultActions actions = mockMvc.perform(
				get("/api/users/mypage")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(MYPAGE_DETAIL_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(MYPAGE_DETAIL_OK.getMessage()))
				.andDo(document(
						"기업 마이페이지 조회 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("User API")
								.summary("마이페이지 조회 API")
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body.name").type(JsonFieldType.STRING).description("이름"),
												fieldWithPath("body.email").type(JsonFieldType.STRING).description("이메일"),
												fieldWithPath("body.enterpriseName").type(JsonFieldType.STRING).description("기업명"),
												fieldWithPath("body.role").type(JsonFieldType.STRING).description("역할 (사원/관리자/기업)"),
												fieldWithPath("body.state").type(JsonFieldType.STRING).description("상태 여부 (pending/active/inactive/rejected)"),
												fieldWithPath("body.employee").type(JsonFieldType.NULL).description("직원 정보")
										)
								)
								.responseSchema(Schema.schema("기업 마이페이지 조회 Response"))
								.build()
						)));
	}

	@Test
	void 개인정보_수정_성공() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(123L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		UserRequest.MyPageModifyDto dto = UserRequest.MyPageModifyDto.builder()
				.department("마케팅팀")
				.empClass("부장")
				.build();

		String content = objectMapper.writeValueAsString(dto);

		doNothing().when(userService).modifyUserDetail(anyLong(), any(UserRequest.MyPageModifyDto.class));

		ResultActions actions = mockMvc.perform(
				put("/api/users/mypage")
						.content(content)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.with(csrf())
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(MYPAGE_MODIFIED_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(MYPAGE_MODIFIED_OK.getMessage()))
				.andDo(document(
						"개인정보 수정 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("User API")
								.summary("개인정보 수정 API")
								.requestFields(
										fieldWithPath("department").description("변경된 부서명"),
										fieldWithPath("empClass").description("변경된 직급명")
								)
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body").ignored()
										)
								)
								.responseSchema(Schema.schema("개인정보 수정 Response"))
								.build()
						)));
	}

	@Test
	void 비밀번호_수정_성공() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(123L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		UserRequest.PasswordModifyDto dto = UserRequest.PasswordModifyDto.builder()
				.newPassword("new1234")
				.build();

		String content = objectMapper.writeValueAsString(dto);

		doNothing().when(userService).modifyPassword(anyLong(), any(UserRequest.PasswordModifyDto.class));

		ResultActions actions = mockMvc.perform(
				patch("/api/users/modify-pwd")
						.content(content)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.with(csrf())
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(PASSWORD_MODIFIED_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(PASSWORD_MODIFIED_OK.getMessage()))
				.andDo(document(
						"비밀번호 수정 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("User API")
								.summary("비밀번호 수정 API")
								.requestFields(
										fieldWithPath("newPassword").description("새 비밀번호")
								)
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body").ignored()
										)
								)
								.responseSchema(Schema.schema("비밀번호 수정 Response"))
								.build()
						)));
	}

	@Test
	void 회원_탈퇴_성공() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(123L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		UserRequest.WithdrawDto dto = UserRequest.WithdrawDto.builder()
				.password("ssafy1234")
				.build();

		String content = objectMapper.writeValueAsString(dto);

		doNothing().when(userService).deleteUser(anyLong(), any(UserRequest.WithdrawDto.class));

		ResultActions actions = mockMvc.perform(
				delete("/api/users/withdraw")
						.content(content)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isNoContent())
				.andExpect(jsonPath("$.header.httpStatusCode").value(USER_DELETE_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(USER_DELETE_OK.getMessage()))
				.andDo(document(
						"회원 탈퇴 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("User API")
								.summary("회원 탈퇴 API")
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body").ignored()
										)
								)
								.build()
						)));
	}

	@Test
	void 회원_탈퇴_실패_잘못된_비밀번호() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(123L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		UserRequest.WithdrawDto dto = UserRequest.WithdrawDto.builder()
				.password("invalide_password")
				.build();

		String content = objectMapper.writeValueAsString(dto);

		doThrow(new BaseException(INVALID_PASSWORD)).when(userService).deleteUser(anyLong(), any(UserRequest.WithdrawDto.class));

		ResultActions actions = mockMvc.perform(
				delete("/api/users/withdraw")
						.content(content)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.header.httpStatusCode").value(INVALID_PASSWORD.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(INVALID_PASSWORD.getMessage()))
				.andDo(document(
						"회원 탈퇴 실패 - 일치하지 않는 비밀번호",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("User API")
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body").type(JsonFieldType.OBJECT).description("에러 상세").optional().ignored()
										)
								)
								.responseSchema(Schema.schema("Error Response"))
								.build()
						)));
	}
}