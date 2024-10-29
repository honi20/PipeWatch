package com.pipewatch.domain.pipelineModel.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipewatch.domain.pipelineModel.model.dto.PipelineModelRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@DisplayName("Pipeline Model API 명세서")
@WithMockUser
class PipelineModelControllerTest {
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
								.tag("Pipeline Model API")
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

	@Test
	void 모델링_파일_업로드_성공() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "sample.gltf", "model/gltf+json", "sample.gltf".getBytes());

		ResultActions actions = mockMvc.perform(
				multipart("/api/models/upload-file")
						.file(file)
						.contentType(MediaType.MULTIPART_FORM_DATA)
						.characterEncoding("UTF-8")
						.with(csrf())
		);

		actions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.header.httpStatusCode").value(FILE_UPLOAD_AND_MODEL_CREATED.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(FILE_UPLOAD_AND_MODEL_CREATED.getMessage()))
				.andDo(document(
						"모델링 파일 업로드 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestParts(
								partWithName("file").description("업로드할 파일 (gltf 확장자만 가능)")
						),
						resource(ResourceSnippetParameters.builder()
								.tag("Pipeline Model API")
								.summary("모델링 파일 업로드 API")
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body.modelId").type(JsonFieldType.NUMBER).description("생성된 모델 Id")
										)
								)
								.build()
						)));
	}

	@Test
	void 모델_초기정보_설정_성공() throws Exception {
		PipelineModelRequest.InitDto dto = PipelineModelRequest.InitDto.builder()
				.name("새로운 모델명")
				.building("역삼 멀티캠퍼스")
				.floor(10)
				.build();

		String content = objectMapper.writeValueAsString(dto);

		ResultActions actions = mockMvc.perform(
				patch("/api/models/init/{modelId}", 1L)
						.content(content)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.with(csrf())
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(MODEL_INIT_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(MODEL_INIT_OK.getMessage()))
				.andDo(document(
						"파이프라인 모델 초기정보 설정 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Pipeline Model API")
								.summary("파이프라인 모델 초기정보 설정 API")
								.pathParameters(
										parameterWithName("modelId").description("모델 Id")
								)
								.requestFields(
										fieldWithPath("name").type(JsonFieldType.STRING).description("모델명"),
										fieldWithPath("building").type(JsonFieldType.STRING).description("건물명"),
										fieldWithPath("floor").type(JsonFieldType.NUMBER).description("층수")
								)
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body").ignored()
										)
								)
								.requestSchema(Schema.schema("파이프라인 모델 초기정보 설정 Request"))
								.build()
						)));
	}

	@Test
	void 모델_정보_수정_성공() throws Exception {
		PipelineModelRequest.ModifyDto dto = PipelineModelRequest.ModifyDto.builder()
				.name("새로운 모델명")
				.description("어제 점검했는데 중단됨.")
				.build();

		String content = objectMapper.writeValueAsString(dto);

		ResultActions actions = mockMvc.perform(
				patch("/api/models/{modelId}", 1L)
						.content(content)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.with(csrf())
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(MODEL_MODIFIED_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(MODEL_MODIFIED_OK.getMessage()))
				.andDo(document(
						"파이프라인 모델 정보 수정 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Pipeline Model API")
								.summary("파이프라인 모델 정보 수정 API")
								.pathParameters(
										parameterWithName("modelId").description("모델 Id")
								)
								.requestFields(
										fieldWithPath("name").type(JsonFieldType.STRING).description("모델명"),
										fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
								)
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body").ignored()
										)
								)
								.requestSchema(Schema.schema("파이프라인 모델 정보 수정 Request"))
								.build()
						)));
	}

	@Test
	void 모델_상세_조회_성공() throws Exception {
		ResultActions actions = mockMvc.perform(
				get("/api/models/{modelId}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(MODEL_DETAIL_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(MODEL_DETAIL_OK.getMessage()))
				.andDo(document(
						"파이프라인 모델 상세조회 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Pipeline Model API")
								.summary("파이프라인 모델 상세조회 API")
								.pathParameters(
										parameterWithName("modelId").description("모델 Id")
								)
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body.name").type(JsonFieldType.STRING).description("모델명"),
												fieldWithPath("body.description").type(JsonFieldType.STRING).description("모델 설명"),
												fieldWithPath("body.modelingUrl").type(JsonFieldType.STRING).description("모델링 파일 url"),
												fieldWithPath("body.isCompleted").type(JsonFieldType.BOOLEAN).description("모델링 완료 여부"),
												fieldWithPath("body.updatedAt").type(JsonFieldType.STRING).description("마지막 수정일"),
												fieldWithPath("body.creator").type(JsonFieldType.OBJECT).description("생성자"),
												fieldWithPath("body.creator.userId").type(JsonFieldType.NUMBER).description("생성자 Id"),
												fieldWithPath("body.creator.userName").type(JsonFieldType.STRING).description("생성자 이름")
										)
								)
								.responseSchema(Schema.schema("파이프라인 모델 상세조회 Response"))
								.build()
						)));
	}

	@Test
	void 모델_삭제_성공() throws Exception {
		ResultActions actions = mockMvc.perform(
				delete("/api/models/{modelId}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isNoContent())
				.andExpect(jsonPath("$.header.httpStatusCode").value(MODEL_DELETED.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(MODEL_DELETED.getMessage()))
				.andDo(document(
						"파이프라인 모델  성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Pipeline Model API")
								.summary("파이프라인 모델 삭제 API")
								.pathParameters(
										parameterWithName("modelId").description("모델 Id")
								)
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body").ignored()
										)
								)
								.responseSchema(Schema.schema("파이프라인 모델 삭제 Response"))
								.build()
						)));
	}

}