package com.pipewatch.domain.user.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipewatch.domain.user.model.dto.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.pipewatch.domain.util.ResponseFieldUtil.getCommonResponseFields;
import static com.pipewatch.global.statusCode.SuccessCode.*;
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
class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void 마이페이지_조회_성공() throws Exception {
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
						"마이페이지 조회 성공",
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
												fieldWithPath("body.empNo").type(JsonFieldType.NUMBER).description("사번"),
												fieldWithPath("body.department").type(JsonFieldType.STRING).description("부서"),
												fieldWithPath("body.empClass").type(JsonFieldType.STRING).description("직급"),
												fieldWithPath("body.roll").type(JsonFieldType.STRING).description("역할 (사원/관리자/기업)"),
												fieldWithPath("body.state").type(JsonFieldType.STRING).description("상태 여부 (pending/active/inactive/rejected)")
										)
								)
								.responseSchema(Schema.schema("마이페이지 조회 Response"))
								.build()
						)));
	}

	@Test
	void 개인정보_수정_성공() throws Exception {
		UserRequest.MyPageModifyDto dto = UserRequest.MyPageModifyDto.builder()
				.department("IT개발팀")
				.empClass("부장")
				.build();

		String content = objectMapper.writeValueAsString(dto);

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
		UserRequest.PasswordModifyDto dto = UserRequest.PasswordModifyDto.builder()
				.newPassword("new1234")
				.build();

		String content = objectMapper.writeValueAsString(dto);

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
		ResultActions actions = mockMvc.perform(
				delete("/api/users/withdraw")
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
}