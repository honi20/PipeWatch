package com.pipewatch.domain.auth.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipewatch.domain.auth.model.dto.AuthDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.pipewatch.domain.util.ResponseFieldUtil.getCommonResponseFields;
import static com.pipewatch.global.statusCode.SuccessCode.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@DisplayName("Auth API 명세서")
@WithMockUser
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 기업_생성_성공() throws Exception {
        AuthDto.EnterpriseRegistRequestDto dto = AuthDto.EnterpriseRegistRequestDto.builder()
                .name("ssafy")
                .industry("제조업")
                .managerEmail("paori@ssafy.com")
                .managerPhoneNumber("010-1234-5678")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        ResultActions actions = mockMvc.perform(
                post("/api/auth/enterprise")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.httpStatusCode").value(ENTERPRISE_CREATED.getHttpStatusCode()))
                .andExpect(jsonPath("$.header.message").value(ENTERPRISE_CREATED.getMessage()))
                .andDo(document(
                    "기업 등록 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
                                .summary("Enterprise 등록 API")
                                .requestFields(
                                        fieldWithPath("name").description("기업명"),
                                        fieldWithPath("industry").description("산업분류"),
                                        fieldWithPath("managerEmail").description("대표관리자 이메일"),
                                        fieldWithPath("managerPhoneNumber").description("대표관리자 전화번호")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").ignored()
                                        )
                                )
                                .requestSchema(Schema.schema("Enterprise 등록 Request"))
                                .build()
                        )));
    }

    @Test
    void 회원가입_성공() throws Exception {
        AuthDto.SignupRequestDto dto = AuthDto.SignupRequestDto.builder()
                .email("kim@ssafy.com")
                .password("ssafy1234")
                .name("김싸피")
                .enterpriseId(1L)
                .empNo(1123456L)
                .department("경영지원부")
                .empClass("사원")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        ResultActions actions = mockMvc.perform(
                post("/api/auth")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.httpStatusCode").value(USER_CREATED.getHttpStatusCode()))
                .andExpect(jsonPath("$.header.message").value(USER_CREATED.getMessage()))
                .andDo(document(
                        "회원가입 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
                                .summary("회원가입 API")
                                .requestFields(
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("password").description("비밀번호"),
                                        fieldWithPath("name").description("이름"),
                                        fieldWithPath("enterpriseId").description("기업 번호"),
                                        fieldWithPath("empNo").description("사번"),
                                        fieldWithPath("department").description("부서"),
                                        fieldWithPath("empClass").description("직급")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").ignored()
                                        )
                                )
                                .requestSchema(Schema.schema("회원가입 Request"))
                                .build()
                        )));
    }

    @Test
    void 인증코드_전송_성공() throws Exception {
        AuthDto.EmailCodeSendRequestDto dto = AuthDto.EmailCodeSendRequestDto.builder()
                .email("kim@ssafy.com")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        ResultActions actions = mockMvc.perform(
                post("/api/auth/send-email-code")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.httpStatusCode").value(EMAIL_CODE_SEND_OK.getHttpStatusCode()))
                .andExpect(jsonPath("$.header.message").value(EMAIL_CODE_SEND_OK.getMessage()))
                .andDo(document(
                        "이메일 인증코드 전송 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
                                .summary("이메일 인증코드 전송 API")
                                .requestFields(
                                        fieldWithPath("email").description("이메일")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").ignored()
                                        )
                                )
                                .requestSchema(Schema.schema("이메일 인증코드 전송 Request"))
                                .build()
                        )));
    }
}