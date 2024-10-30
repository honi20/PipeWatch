package com.pipewatch.domain.enterprise.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipewatch.domain.auth.model.dto.AuthRequest;
import com.pipewatch.domain.enterprise.model.dto.EnterpriseResponse;
import com.pipewatch.domain.enterprise.service.EnterpriseService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.pipewatch.domain.util.ResponseFieldUtil.getCommonResponseFields;
import static com.pipewatch.global.statusCode.SuccessCode.ENTERPRISE_DETAIL_OK;
import static com.pipewatch.global.statusCode.SuccessCode.MYPAGE_DETAIL_OK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
@DisplayName("Enterprise API 명세서")
@WithMockUser
class EnterpriseControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private EnterpriseService enterpriseService;

	@Test
	void 기업정보_조회_성공() throws Exception {
		EnterpriseResponse.DetailDto response = EnterpriseResponse.DetailDto.builder()
				.name("paori")
				.industry("제조업")
				.managerEmail("admin@paori.com")
				.managerPhoneNumber("010-1234-5678")
				.build();

		when(enterpriseService.detailEnterprise()).thenReturn(response);

		ResultActions actions = mockMvc.perform(
				get("/api/enterprises")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(ENTERPRISE_DETAIL_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(ENTERPRISE_DETAIL_OK.getMessage()))
				.andDo(document(
						"기업정보 조회 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Enterprise API")
								.summary("기업정보 조회 API")
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body.name").type(JsonFieldType.STRING).description("기업명"),
												fieldWithPath("body.industry").type(JsonFieldType.STRING).description("산업분야"),
												fieldWithPath("body.managerEmail").type(JsonFieldType.STRING).description("대표관리자 이메일"),
												fieldWithPath("body.managerPhoneNumber").type(JsonFieldType.STRING).description("대표관리자 전화번호")
										)
								)
								.responseSchema(Schema.schema("기업정보 조회 Response"))
								.build()
						)));
	}
}