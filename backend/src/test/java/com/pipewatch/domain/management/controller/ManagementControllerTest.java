package com.pipewatch.domain.management.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipewatch.domain.management.model.dto.ManagementRequest;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
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
												fieldWithPath("body.employees[].uuid").type(JsonFieldType.STRING).description("직원 UUID"),
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
												fieldWithPath("body.employees[].uuid").type(JsonFieldType.STRING).description("직원 UUID"),
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

	@Test
	void 접근_권한_수정_성공() throws Exception {
		ManagementRequest.AccessModifyDto dto = ManagementRequest.AccessModifyDto.builder()
				.newRoll("관리자")
				.build();

		String content = objectMapper.writeValueAsString(dto);

		ResultActions actions = mockMvc.perform(
				patch("/api/management/{userUuid}", "ab23cde")
						.content(content)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.with(csrf())
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(ROLL_MODIFIED_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(ROLL_MODIFIED_OK.getMessage()))
				.andDo(document(
						"접근 권한 변경 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Management API")
								.summary("접근 권한 변경 API")
								.pathParameters(
										parameterWithName("userUuid").description("유저 UUID")
								)
								.requestFields(
										fieldWithPath("newRoll").type(JsonFieldType.STRING).description("새 역할")
								)
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body").ignored()
										)
								)
								.requestSchema(Schema.schema("접근 권한 변경 Request"))
								.build()
						)));
	}

	@Test
	void 직원_검색_성공() throws Exception {
		ResultActions actions = mockMvc.perform(
				get("/api/management/search?keyword=싸피")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(EMPLOYEE_SEARCH_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(EMPLOYEE_SEARCH_OK.getMessage()))
				.andDo(document(
						"직원 검색 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Management API")
								.summary("직원 검색 API")
								.queryParameters(
										parameterWithName("keyword").description("검색어").optional()
								)
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body.employees[]").type(JsonFieldType.ARRAY).description("직원 리스트"),
												fieldWithPath("body.employees[].uuid").type(JsonFieldType.STRING).description("직원 UUID"),
												fieldWithPath("body.employees[].name").type(JsonFieldType.STRING).description("직원 이름"),
												fieldWithPath("body.employees[].email").type(JsonFieldType.STRING).description("직원 이메일"),
												fieldWithPath("body.employees[].empNo").type(JsonFieldType.NUMBER).description("직원 사번"),
												fieldWithPath("body.employees[].department").type(JsonFieldType.STRING).description("직원 부서"),
												fieldWithPath("body.employees[].empClass").type(JsonFieldType.STRING).description("직원 직급"),
												fieldWithPath("body.employees[].role").type(JsonFieldType.STRING).description("직원 역할 (사원/관리자/기업)")
										)
								)
								.responseSchema(Schema.schema("직원 검색 Response"))
								.build()
						)));

	}

	@Test
	void 건물_리스트_조회_성공() throws Exception {
		ResultActions actions = mockMvc.perform(
				get("/api/management/buildings")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(BUILDING_LIST_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(BUILDING_LIST_OK.getMessage()))
				.andDo(document(
						"건물 목록 조회 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Management API")
								.summary("건물 목록 조회 API")
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body.buildings[]").type(JsonFieldType.ARRAY).description("건물 리스트"),
												fieldWithPath("body.buildings[].building").type(JsonFieldType.STRING).description("건물명"),
												fieldWithPath("body.buildings[].floors[]").type(JsonFieldType.ARRAY).description("층수 리스트")
										)
								)
								.responseSchema(Schema.schema("건물 목록 조회 Response"))
								.build()
						)));

	}
}