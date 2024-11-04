package com.pipewatch.domain.management.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipewatch.domain.management.model.dto.ManagementRequest;
import com.pipewatch.domain.management.model.dto.ManagementResponse;
import com.pipewatch.domain.management.service.ManagementService;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.pipewatch.domain.util.ResponseFieldUtil.getCommonResponseFields;
import static com.pipewatch.global.statusCode.ErrorCode.FORBIDDEN_USER_ROLE;
import static com.pipewatch.global.statusCode.SuccessCode.*;
import static org.mockito.ArgumentMatchers.any;
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
@DisplayName("Management API 명세서")
@WithMockUser
@ActiveProfiles("test")
class ManagementControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ManagementService managementService;

	@Test
	void 승인대기_리스트_조회_성공() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(124L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		ManagementResponse.EmployeeDto employee = new ManagementResponse.EmployeeDto("1604b772-adc0-4212-8a90-81186c57f598", "테스트", "test@ssafy.com", 1243242L, "IT사업부", "팀장", "ROLE_USER");
		ManagementResponse.EmployeeWaitingListDto response = ManagementResponse.EmployeeWaitingListDto.builder()
				.employees(List.of(employee))
				.build();

		when(managementService.getWaitingEmployeeList(124L)).thenReturn(response);

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
	void 승인대기_리스트_조회_실패_권한_없음() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(123L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		doThrow(new BaseException(FORBIDDEN_USER_ROLE)).when(managementService).getWaitingEmployeeList(123L);

		ResultActions actions = mockMvc.perform(
				get("/api/management/waiting-list")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
		);

		actions
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.header.httpStatusCode").value(FORBIDDEN_USER_ROLE.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(FORBIDDEN_USER_ROLE.getMessage()))
				.andDo(document(
						"승인대기 직원 리스트 조회 실패 - 기업 유저만 조회 가능",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Management API")
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body").type(JsonFieldType.OBJECT).description("에러 상세").optional().ignored()
										)
								)
								.responseSchema(Schema.schema("Error Response"))
								.build()
						)));
	}

	@Test
	void 직원_리스트_조회_성공() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(124L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		ManagementResponse.EmployeeDto employee1 = new ManagementResponse.EmployeeDto("1604b772-adc0-4212-8a90-81186c57f600", "최싸피", "choi@ssafy.com", 1534534L, "마케팅부", "대리", "ROLE_EMPLOYEE");
		ManagementResponse.EmployeeDto employee2 = new ManagementResponse.EmployeeDto("1604b772-adc0-4212-8a90-81186c57f601", "김싸피", "kim@ssafy.com", 1423435L, "인사부", "부장", "ROLE_ADMIN");
		ManagementResponse.EmployeeListDto response = ManagementResponse.EmployeeListDto.builder()
				.employees(List.of(employee1, employee2))
				.build();

		when(managementService.getEmployeeList(124L)).thenReturn(response);

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
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(124L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		doNothing().when(managementService).modifyUserRoll(anyLong(), any(ManagementRequest.AccessModifyDto.class));

		ManagementRequest.AccessModifyDto dto = ManagementRequest.AccessModifyDto.builder()
				.userUuid("1604b772-adc0-4212-8a90-81186c57f600")
				.newRole("ROLE_ADMIN")
				.build();

		String content = objectMapper.writeValueAsString(dto);

		ResultActions actions = mockMvc.perform(
				patch("/api/management")
						.content(content)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.with(csrf())
		);

		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.header.httpStatusCode").value(ROLE_MODIFIED_OK.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(ROLE_MODIFIED_OK.getMessage()))
				.andDo(document(
						"접근 권한 변경 성공",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Management API")
								.summary("접근 권한 변경 API")
								.requestFields(
										fieldWithPath("userUuid").type(JsonFieldType.STRING).description("직원 UUID"),
										fieldWithPath("newRole").type(JsonFieldType.STRING).description("새 역할")
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
	void 접근_권한_수정_실패_권한_없음() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(123L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		doThrow(new BaseException(FORBIDDEN_USER_ROLE)).when(managementService).modifyUserRoll(anyLong(), any(ManagementRequest.AccessModifyDto.class));

		ManagementRequest.AccessModifyDto dto = ManagementRequest.AccessModifyDto.builder()
				.userUuid("1604b772-adc0-4212-8a90-81186c57f600")
				.newRole("ROLE_ADMIN")
				.build();

		String content = objectMapper.writeValueAsString(dto);

		ResultActions actions = mockMvc.perform(
				patch("/api/management")
						.content(content)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.with(csrf())
		);

		actions
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.header.httpStatusCode").value(FORBIDDEN_USER_ROLE.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(FORBIDDEN_USER_ROLE.getMessage()))
				.andDo(document(
						"접근 권한 수정 실패 - 기업 유저만 수정 가능",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Management API")
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body").type(JsonFieldType.OBJECT).description("에러 상세").optional().ignored()
										)
								)
								.responseSchema(Schema.schema("Error Response"))
								.build()
						)));
	}

	@Test
	void 접근_권한_수정_실패_존재하지_않는_역할() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(124L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		doThrow(new BaseException(FORBIDDEN_USER_ROLE)).when(managementService).modifyUserRoll(anyLong(), any(ManagementRequest.AccessModifyDto.class));

		ManagementRequest.AccessModifyDto dto = ManagementRequest.AccessModifyDto.builder()
				.userUuid("1604b772-adc0-4212-8a90-81186c57f600")
				.newRole("ROLE_NOROLE")
				.build();

		String content = objectMapper.writeValueAsString(dto);

		ResultActions actions = mockMvc.perform(
				patch("/api/management")
						.content(content)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.with(csrf())
		);

		actions
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.header.httpStatusCode").value(FORBIDDEN_USER_ROLE.getHttpStatusCode()))
				.andExpect(jsonPath("$.header.message").value(FORBIDDEN_USER_ROLE.getMessage()))
				.andDo(document(
						"접근 권한 수정 실패 - 존재하지 않는 Role",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						resource(ResourceSnippetParameters.builder()
								.tag("Management API")
								.responseFields(
										getCommonResponseFields(
												fieldWithPath("body").type(JsonFieldType.OBJECT).description("에러 상세").optional().ignored()
										)
								)
								.responseSchema(Schema.schema("Error Response"))
								.build()
						)));
	}

	@Test
	void 직원_검색_성공() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(124L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		ManagementResponse.EmployeeDto employee1 = new ManagementResponse.EmployeeDto("1604b772-adc0-4212-8a90-81186c57f600", "최싸피", "choi@ssafy.com", 1534534L, "마케팅부", "대리", "ROLE_EMPLOYEE");
		ManagementResponse.EmployeeDto employee2 = new ManagementResponse.EmployeeDto("1604b772-adc0-4212-8a90-81186c57f601", "김싸피", "kim@ssafy.com", 1423435L, "인사부", "부장", "ROLE_ADMIN");
		ManagementResponse.EmployeeSearchDto response = ManagementResponse.EmployeeSearchDto.builder()
				.employees(List.of(employee1, employee2))
				.build();

		when(managementService.searchEmployee(124L, "싸피")).thenReturn(response);

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
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(123L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
		);

		ManagementResponse.BuildingDto building1 = new ManagementResponse.BuildingDto("역삼 멀티캠퍼스", List.of(12, 14));
		ManagementResponse.BuildingDto building2 = new ManagementResponse.BuildingDto("부울경 멀티캠퍼스", List.of(1, 3));

		// BuildingListDto 빌드
		ManagementResponse.BuildingListDto response = ManagementResponse.BuildingListDto.builder()
				.buildings(List.of(building1, building2))
				.build();

		when(managementService.getBuildingList(123L)).thenReturn(response);

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