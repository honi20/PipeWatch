package com.pipewatch.domain.management.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static com.pipewatch.global.statusCode.SuccessCode.EMPLOYEE_LIST_OK;
import static com.pipewatch.global.statusCode.SuccessCode.WAITING_EMPLOYEE_LIST_OK;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@DisplayName("Management API 명세서")
@WithMockUser
class ManagementControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void 승인대기_리스트_조회_성공() throws Exception {
		ResultActions actions = mockMvc.perform(
				get("/api/management/waiting-list")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(WAITING_EMPLOYEE_LIST_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(WAITING_EMPLOYEE_LIST_OK.getMessage()))
				.andDo(document(
						"승인대기 직원 리스트 조회 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Management API")
								.summary("승인대기 직원 리스트 조회 API")
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body.employees[]").type(JsonFieldType.ARRAY).description("승인대기 중인 직원 리스트"),
												fieldWithPath("body.employees[].name").type(JsonFieldType.STRING).description("직원 이름"),
												fieldWithPath("body.employees[].email").type(JsonFieldType.STRING).description("직원 이메일"),
												fieldWithPath("body.employees[].empNo").type(JsonFieldType.NUMBER).description("직원 사번"),
												fieldWithPath("body.employees[].department").type(JsonFieldType.STRING).description("직원 부서"),
												fieldWithPath("body.employees[].empClass").type(JsonFieldType.STRING).description("직원 직급"),
												fieldWithPath("body.employees[].role").type(JsonFieldType.STRING).description("직원 역할 (사원/관리자/기업)")
										)
								)
								.responseSchema(Schema.schema("승인대기 직원 리스트 조회 Response"))
								.build()
						)));
	}

	@Test
	void 직원_리스트_조회_성공() throws Exception {
		ResultActions actions = mockMvc.perform(
				get("/api/management")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(EMPLOYEE_LIST_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(EMPLOYEE_LIST_OK.getMessage()))
				.andDo(document(
						"직원 리스트 조회 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Management API")
								.summary("직원 리스트 조회 API")
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body.employees[]").type(JsonFieldType.ARRAY).description("직원 리스트"),
												fieldWithPath("body.employees[].name").type(JsonFieldType.STRING).description("직원 이름"),
												fieldWithPath("body.employees[].email").type(JsonFieldType.STRING).description("직원 이메일"),
												fieldWithPath("body.employees[].empNo").type(JsonFieldType.NUMBER).description("직원 사번"),
												fieldWithPath("body.employees[].department").type(JsonFieldType.STRING).description("직원 부서"),
												fieldWithPath("body.employees[].empClass").type(JsonFieldType.STRING).description("직원 직급"),
												fieldWithPath("body.employees[].role").type(JsonFieldType.STRING).description("직원 역할 (사원/관리자/기업)")
										)
								)
								.responseSchema(Schema.schema("직원 리스트 조회 Response"))
								.build()
						)));
	}
}