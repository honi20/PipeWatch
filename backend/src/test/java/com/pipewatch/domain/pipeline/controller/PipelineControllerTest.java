package com.pipewatch.domain.pipeline.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipewatch.domain.pipeline.model.dto.PipelineRequest;
import com.pipewatch.domain.pipeline.model.dto.PipelineResponse;
import com.pipewatch.domain.pipelineModel.model.dto.PipelineModelRequest;
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
import static com.pipewatch.global.statusCode.SuccessCode.*;
import static org.junit.jupiter.api.Assertions.*;
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
@DisplayName("Pipeline API 명세서")
@WithMockUser
class PipelineControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void 파이프라인_조회_성공() throws Exception {
		ResultActions actions = mockMvc.perform(
				get("/api/pipelines/{pipelineUuid}", "pipeline_abd34412jd_1")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(PIPELINE_DETAIL_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(PIPELINE_DETAIL_OK.getMessage()))
				.andDo(document(
						"단일 파이프라인 상세조회 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Pipeline API")
								.summary("단일 파이프라인 상세조회 API")
								.pathParameters(
										parameterWithName("pipelineUuid").description("파이프라인 Uuid")
								)
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body.name").type(JsonFieldType.STRING).description("파이프라인 이름"),
												fieldWithPath("body.updatedAt").type(JsonFieldType.STRING).description("마지막 점검일"),
												fieldWithPath("body.property").type(JsonFieldType.OBJECT).description("파이프라인 속성"),
												fieldWithPath("body.property.pipeMaterial").type(JsonFieldType.STRING).description("파이프 재질"),
												fieldWithPath("body.property.outerDiameter").type(JsonFieldType.NUMBER).description("파이프 외경"),
												fieldWithPath("body.property.innerDiameter").type(JsonFieldType.NUMBER).description("파이프 내경"),
												fieldWithPath("body.property.fluidMaterial").type(JsonFieldType.STRING).description("유체 재질"),
												fieldWithPath("body.property.velocity").type(JsonFieldType.NUMBER).description("유체 유속"),
												fieldWithPath("body.defects[]").type(JsonFieldType.ARRAY).description("파이프라인 결함 리스트"),
												fieldWithPath("body.defects[].position").type(JsonFieldType.OBJECT).description("파이프라인 결함 좌표"),
												fieldWithPath("body.defects[].position.x").type(JsonFieldType.NUMBER).description("x좌표"),
												fieldWithPath("body.defects[].position.y").type(JsonFieldType.NUMBER).description("y좌표"),
												fieldWithPath("body.defects[].position.z").type(JsonFieldType.NUMBER).description("z좌표"),
												fieldWithPath("body.defects[].type").type(JsonFieldType.STRING).description("파이프라인 결함 타입 (CORROSION/CRACK)")
										)
								)
								.responseSchema(Schema.schema("단일 파이프라인 상세조회 Response"))
								.build()
						)));
	}

	@Test
	void 파이프라인_수정_성공() throws Exception {
		PipelineRequest.ModifyDto dto = PipelineRequest.ModifyDto.builder()
				.name("변경된 파이프라인 이름")
				.build();

		String content = objectMapper.writeValueAsString(dto);

		ResultActions actions = mockMvc.perform(
				put("/api/pipelines/{pipelineUuid}", "pipeline_abd34412jd_1")
						.content(content)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.with(csrf())
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(PIPELINE_MODIFIED_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(PIPELINE_MODIFIED_OK.getMessage()))
				.andDo(document(
						"파이프라인 정보 수정 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Pipeline API")
								.summary("파이프라인 정보 수정 API")
								.pathParameters(
										parameterWithName("pipelineUuid").description("파이프라인 Uuid")
								)
								.requestFields(
										fieldWithPath("name").type(JsonFieldType.STRING).description("단일 파이프라인 이름")
								)
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body").ignored()
										)
								)
								.requestSchema(Schema.schema("파이프라인 정보 수정 Request"))
								.build()
						)));
	}

	@Test
	void 파이프라인_속성_수정_성공() throws Exception {
		PipelineRequest.ModifyPropertiesDto dto = PipelineRequest.ModifyPropertiesDto.builder()
				.pipeMaterial("Aluminum")
				.outerDiameter(150.0)
				.innerDiameter(10.0)
				.fluidMaterial("Water")
				.velocity(1.0)
				.build();

		String content = objectMapper.writeValueAsString(dto);

		ResultActions actions = mockMvc.perform(
				put("/api/pipelines/{pipelineUuid}/properties", "pipeline_abd34412jd_1")
						.content(content)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.with(csrf())
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(PIPELINE_PROPERTY_MODIFIED_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(PIPELINE_PROPERTY_MODIFIED_OK.getMessage()))
				.andDo(document(
						"파이프라인 속성 정보 수정 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Pipeline API")
								.summary("파이프라인 속성 정보 수정 API")
								.pathParameters(
										parameterWithName("pipelineUuid").description("파이프라인 Uuid")
								)
								.requestFields(
										fieldWithPath("pipeMaterial").type(JsonFieldType.STRING).description("파이프 재질"),
										fieldWithPath("outerDiameter").type(JsonFieldType.NUMBER).description("파이프 외경"),
										fieldWithPath("innerDiameter").type(JsonFieldType.NUMBER).description("파이프 내경"),
										fieldWithPath("fluidMaterial").type(JsonFieldType.STRING).description("유체 재질"),
										fieldWithPath("velocity").type(JsonFieldType.NUMBER).description("유체 유속")
								)
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body").ignored()
										)
								)
								.requestSchema(Schema.schema("파이프라인 속성 정보 수정 Request"))
								.build()
						)));
	}
}