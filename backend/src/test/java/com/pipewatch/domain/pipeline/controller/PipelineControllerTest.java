package com.pipewatch.domain.pipeline.controller;

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

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.pipewatch.domain.util.ResponseFieldUtil.getCommonResponseFields;
import static com.pipewatch.global.statusCode.SuccessCode.MODEL_LIST_OK;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@DisplayName("Pipeline API 명세서")
@WithMockUser
class PipelineControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void 모델_리스트_조회_성공() throws Exception {
		ResultActions actions = mockMvc.perform(
				get("/api/models")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(MODEL_LIST_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(MODEL_LIST_OK.getMessage()))
				.andDo(document(
						"파이프라인 모델 리스트 조회 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Pipeline API")
								.summary("파이프라인 모델 리스트 조회 API")
								.queryParameters(
										parameterWithName("building").description("건물명").optional(),
										parameterWithName("floor").description("층수").optional()
								)
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body.buildings[]").type(JsonFieldType.ARRAY).description("건물 리스트"),
												fieldWithPath("body.buildings[].building").type(JsonFieldType.STRING).description("건물명"),
												fieldWithPath("body.buildings[].floors[]").type(JsonFieldType.ARRAY).description("층수 리스트"),
												fieldWithPath("body.buildings[].floors[].floor").type(JsonFieldType.NUMBER).description("층"),
												fieldWithPath("body.buildings[].floors[].models[]").type(JsonFieldType.ARRAY).description("파이프 모델 리스트"),
												fieldWithPath("body.buildings[].floors[].models[].modelId").type(JsonFieldType.NUMBER).description("파이프모델 Id"),
												fieldWithPath("body.buildings[].floors[].models[].name").type(JsonFieldType.STRING).description("파이프모델명"),
												fieldWithPath("body.buildings[].floors[].models[].previewUrl").type(JsonFieldType.STRING).description("파이프모델 미리보기 이미지"),
												fieldWithPath("body.buildings[].floors[].models[].updatedAt").type(JsonFieldType.STRING).description("마지막 수정일")
										)
								)
								.responseSchema(Schema.schema("파이프라인 모델 리스트 조회 Response"))
								.build()
						)));
	}
}