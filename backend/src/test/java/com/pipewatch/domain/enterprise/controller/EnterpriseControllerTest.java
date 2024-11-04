package com.pipewatch.domain.enterprise.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipewatch.domain.enterprise.model.dto.EnterpriseResponse;
import com.pipewatch.domain.enterprise.service.EnterpriseService;
import com.pipewatch.global.jwt.service.JwtService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.pipewatch.domain.util.ResponseFieldUtil.getCommonResponseFields;
import static com.pipewatch.global.statusCode.SuccessCode.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@DisplayName("Enterprise API 명세서")
@WithMockUser
@ActiveProfiles("test")
class EnterpriseControllerTest {
	private final static String UUID = "1604b772-adc0-4212-8a90-81186c57f598";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private EnterpriseService enterpriseService;

	@Autowired
	private JwtService jwtService;

	private String jwtToken;

	@Test
	void 기업정보_조회_성공() throws Exception {
		jwtToken = jwtService.createAccessToken(UUID);

		EnterpriseResponse.DetailDto response = EnterpriseResponse.DetailDto.builder()
				.name("paori")
				.industry("제조업")
				.managerEmail("admin@ssafy.com")
				.managerPhoneNumber("010-1234-5678")
				.build();

		when(enterpriseService.getEnterpriseDetail(anyLong())).thenReturn(response);

		ResultActions actions = mockMvc.perform(
				get("/api/enterprises")
						.header("Authorization", "Bearer " + jwtToken)
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
								.requestHeaders(
										headerWithName("Authorization").description("Access Token")
								)
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

	@Test
	void 기업_리스트_조회_성공() throws Exception {
		EnterpriseResponse.EnterpriseDto enterprise1 = new EnterpriseResponse.EnterpriseDto(1L, "paori", "제조업");
		EnterpriseResponse.EnterpriseDto enterprise2 = new EnterpriseResponse.EnterpriseDto(2L, "samsung", "제조업");
		EnterpriseResponse.ListDto response = EnterpriseResponse.ListDto.builder()
				.enterprises(List.of(enterprise1, enterprise2))
				.build();

		when(enterpriseService.getEnterpriseList()).thenReturn(response);

		ResultActions actions = mockMvc.perform(
				get("/api/enterprises/list")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(ENTERPRISE_LIST_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(ENTERPRISE_LIST_OK.getMessage()))
				.andDo(document(
						"기업 리스트 조회 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Enterprise API")
								.summary("기업 리스트 조회 API")
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body.enterprises[]").type(JsonFieldType.ARRAY).description("기업리스트"),
												fieldWithPath("body.enterprises[].enterpriseId").type(JsonFieldType.NUMBER).description("기업 ID"),
												fieldWithPath("body.enterprises[].name").type(JsonFieldType.STRING).description("기업명"),
												fieldWithPath("body.enterprises[].industry").type(JsonFieldType.STRING).description("기업 산업")
										)
								)
								.responseSchema(Schema.schema("기업 리스트 조회 Response"))
								.build()
						)));
	}

	@Test
	void 건물_층_리스트_조회_성공() throws Exception {
		jwtToken = jwtService.createAccessToken(UUID);

		EnterpriseResponse.BuildingAndFloorDto building1 = new EnterpriseResponse.BuildingAndFloorDto("역삼 멀티캠퍼스", List.of(12, 14));
		EnterpriseResponse.BuildingAndFloorDto building2 = new EnterpriseResponse.BuildingAndFloorDto("부울경 멀티캠퍼스", List.of(1, 3));

		// BuildingListDto 빌드
		EnterpriseResponse.BuildingAndFloorListDto response = EnterpriseResponse.BuildingAndFloorListDto.builder()
				.buildings(List.of(building1, building2))
				.build();

		when(enterpriseService.getBuildingAndFloorList(anyLong())).thenReturn(response);

		ResultActions actions = mockMvc.perform(
				get("/api/enterprises/floors")
						.header("Authorization", "Bearer " + jwtToken)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(BUILDING_AND_FLOOR_LIST_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(BUILDING_AND_FLOOR_LIST_OK.getMessage()))
				.andDo(document(
						"건물 및 층수 목록 조회 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Management API")
								.summary("건물 및 층수 목록 조회 API")
								.requestHeaders(
										headerWithName("Authorization").description("Access Token")
								)
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body.buildings[]").type(JsonFieldType.ARRAY).description("건물 리스트"),
												fieldWithPath("body.buildings[].building").type(JsonFieldType.STRING).description("건물명"),
												fieldWithPath("body.buildings[].floors[]").type(JsonFieldType.ARRAY).description("층수 리스트")
										)
								)
								.responseSchema(Schema.schema("건물 및 층수 목록 조회 Response"))
								.build()
						)));

	}

}