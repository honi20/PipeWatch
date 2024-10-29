package com.pipewatch.domain.user.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.apache.bcel.generic.Type;
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
import static com.pipewatch.global.statusCode.SuccessCode.MYPAGE_OK;
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
				get("/api/user/mypage")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(MYPAGE_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(MYPAGE_OK.getMessage()))
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
}