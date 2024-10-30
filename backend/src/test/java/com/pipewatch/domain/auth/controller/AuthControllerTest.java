package com.pipewatch.domain.auth.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipewatch.domain.auth.model.dto.AuthRequest;
import com.pipewatch.domain.auth.model.dto.AuthResponse;
import com.pipewatch.domain.auth.service.AuthService;
import com.pipewatch.global.exception.BaseException;
import com.pipewatch.global.jwt.entity.JwtToken;
import com.pipewatch.global.mail.MailService;
import com.pipewatch.global.redis.RedisUtil;
import io.swagger.v3.core.util.Json;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.pipewatch.domain.util.ResponseFieldUtil.getCommonResponseFields;
import static com.pipewatch.global.statusCode.ErrorCode.*;
import static com.pipewatch.global.statusCode.SuccessCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
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
@ActiveProfiles("test")
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;


    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private MailService mailService;

    @Test
    void 인증코드_전송_성공() throws Exception {
        AuthRequest.EmailCodeSendDto dto = AuthRequest.EmailCodeSendDto.builder()
                .email("paori@ssafy.com")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        given(mailService.sendVerifyEmail(dto.getEmail())).willReturn("239123");

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

    @Test
    void 인증코드_전송_실패_중복된_이메일() throws Exception {
        AuthRequest.EmailCodeSendDto dto = AuthRequest.EmailCodeSendDto.builder()
                .email("test@ssafy.com")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        doThrow(new BaseException(DUPLICATED_EMAIL)).when(authService).sendEmailCode(any(AuthRequest.EmailCodeSendDto.class));

        ResultActions actions = mockMvc.perform(
                post("/api/auth/send-email-code")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        actions
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.header.httpStatusCode").value(DUPLICATED_EMAIL.getHttpStatusCode()))
                .andExpect(jsonPath("$.header.message").value(DUPLICATED_EMAIL.getMessage()))
                .andDo(document(
                        "이메일 인증코드 전송 실패 - 이미 등록된 이메일",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
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
    void 인증코드_확인_성공() throws Exception {
        AuthRequest.EmailCodeVerifyDto dto = AuthRequest.EmailCodeVerifyDto.builder()
                .email("paori@ssafy.com")
                .verifyCode("239123")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        given(redisUtil.getData(dto.getEmail() + "_verify")).willReturn(new JwtToken(null, null, null, "239123"));

        ResultActions actions = mockMvc.perform(
                post("/api/auth/verify-email-code")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.httpStatusCode").value(EMAIL_VERIFIED.getHttpStatusCode()))
                .andExpect(jsonPath("$.header.message").value(EMAIL_VERIFIED.getMessage()))
                .andDo(document(
                        "이메일 인증코드 확인 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
                                .summary("이메일 인증코드 확인 API")
                                .requestFields(
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("verifyCode").description("인증코드")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").ignored()
                                        )
                                )
                                .requestSchema(Schema.schema("이메일 인증코드 확인 Request"))
                                .build()
                        )));
    }

    @Test
    void 인증코드_확인_실패_존재하지_않는_인증정보() throws Exception {
        AuthRequest.EmailCodeVerifyDto dto = AuthRequest.EmailCodeVerifyDto.builder()
                .email("nonono@ssafy.com")
                .verifyCode("999999")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        doThrow(new BaseException(VERIFY_NOT_FOUND)).when(authService).verifyEmailCode(any(AuthRequest.EmailCodeVerifyDto.class));

        ResultActions actions = mockMvc.perform(
                post("/api/auth/verify-email-code")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.httpStatusCode").value(VERIFY_NOT_FOUND.getHttpStatusCode()))
                .andExpect(jsonPath("$.header.message").value(VERIFY_NOT_FOUND.getMessage()))
                .andDo(document(
                        "이메일 인증코드 확인 실패 - 인증정보가 존재하지 않는 이메일",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
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
    void 인증코드_확인_실패_잘못된_인증코드() throws Exception {
        AuthRequest.EmailCodeVerifyDto dto = AuthRequest.EmailCodeVerifyDto.builder()
                .email("paori@ssafy.com")
                .verifyCode("999999")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        given(redisUtil.getData(dto.getEmail() + "_verify")).willReturn(new JwtToken(null, null, null, "239123"));

        doThrow(new BaseException(INVALID_EMAIL_CODE)).when(authService).verifyEmailCode(any(AuthRequest.EmailCodeVerifyDto.class));

        ResultActions actions = mockMvc.perform(
                post("/api/auth/verify-email-code")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        actions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.header.httpStatusCode").value(INVALID_EMAIL_CODE.getHttpStatusCode()))
                .andExpect(jsonPath("$.header.message").value(INVALID_EMAIL_CODE.getMessage()))
                .andDo(document(
                        "이메일 인증코드 확인 실패 - 잘못된 이메일 인증코드",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
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
    void 회원가입_성공() throws Exception {
        AuthRequest.SignupDto dto = AuthRequest.SignupDto.builder()
                .email("paori@ssafy.com")
                .password("ssafy1234")
                .name("김싸피")
                .enterpriseId(1L)
                .empNo(1123456L)
                .department("경영지원부")
                .empClass("사원")
                .verifyCode("sampleCode")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        AuthResponse.AccessTokenDto response = AuthResponse.AccessTokenDto.builder()
                .accessToken("sampleAccessToken")
                .build();

        when(authService.signup(any(AuthRequest.SignupDto.class))).thenReturn(String.valueOf(response));

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
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("enterpriseId").type(JsonFieldType.NUMBER).description("기업 번호"),
                                        fieldWithPath("empNo").type(JsonFieldType.NUMBER).description("사번"),
                                        fieldWithPath("department").type(JsonFieldType.STRING).description("부서"),
                                        fieldWithPath("empClass").type(JsonFieldType.STRING).description("직급"),
                                        fieldWithPath("verifyCode").type(JsonFieldType.STRING).description("이메일 인증 코드")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.accessToken").type(JsonFieldType.STRING).description("access token")
                                        )
                                )
                                .requestSchema(Schema.schema("회원가입 Request"))
                                .build()
                        )));
    }

    @Test
    void 회원가입_실패_존재하는_이메일() throws Exception {
        AuthRequest.SignupDto dto = AuthRequest.SignupDto.builder()
                .email("test@ssafy.com")
                .password("ssafy1234")
                .name("김싸피")
                .enterpriseId(1L)
                .empNo(1123456L)
                .department("경영지원부")
                .empClass("사원")
                .verifyCode("sampleCode")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        doThrow(new BaseException(DUPLICATED_EMAIL)).when(authService).signup(any(AuthRequest.SignupDto.class));

        ResultActions actions = mockMvc.perform(
                multipart("/api/auth")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        actions
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.header.httpStatusCode").value(DUPLICATED_EMAIL.getHttpStatusCode()))
                .andExpect(jsonPath("$.header.message").value(DUPLICATED_EMAIL.getMessage()))
                .andDo(document(
                        "회원가입 실패 - 이미 등록된 이메일인 경우",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(JsonFieldType.OBJECT).description("에러 상세").optional().ignored()
                                        )
                                )
                                .responseSchema(Schema.schema("Error Response"))
                                .build()
                        ))
                );
    }

    @Test
    void 회원가입_실패_존재하지_않는_기업() throws Exception {
        AuthRequest.SignupDto dto = AuthRequest.SignupDto.builder()
                .email("test@ssafy.com")
                .password("ssafy1234")
                .name("김싸피")
                .enterpriseId(999L)
                .empNo(1123456L)
                .department("경영지원부")
                .empClass("사원")
                .verifyCode("sampleCode")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        doThrow(new BaseException(ENTERPRISE_NOT_FOUND)).when(authService).signup(any(AuthRequest.SignupDto.class));

        ResultActions actions = mockMvc.perform(
                multipart("/api/auth")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.httpStatusCode").value(ENTERPRISE_NOT_FOUND.getHttpStatusCode()))
                .andExpect(jsonPath("$.header.message").value(ENTERPRISE_NOT_FOUND.getMessage()))
                .andDo(document(
                        "회원가입 실패 - 존재하지 않는 기업",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(JsonFieldType.OBJECT).description("에러 상세").optional().ignored()
                                        )
                                )
                                .responseSchema(Schema.schema("Error Response"))
                                .build()
                        ))
                );
    }

    @Test
    void 회원가입_실패_잘못된_이메일_형식() throws Exception {
        AuthRequest.SignupDto dto = AuthRequest.SignupDto.builder()
                .email("test@none.com")
                .password("ssafy1234")
                .name("김싸피")
                .enterpriseId(999L)
                .empNo(1123456L)
                .department("경영지원부")
                .empClass("사원")
                .verifyCode("sampleCode")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        doThrow(new BaseException(INVALID_EMAIL_FORMAT)).when(authService).signup(any(AuthRequest.SignupDto.class));

        ResultActions actions = mockMvc.perform(
                multipart("/api/auth")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        actions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.header.httpStatusCode").value(INVALID_EMAIL_FORMAT.getHttpStatusCode()))
                .andExpect(jsonPath("$.header.message").value(INVALID_EMAIL_FORMAT.getMessage()))
                .andDo(document(
                        "회원가입 실패 - 잘못된 이메일 형식 (기업 도메인과 형식이 같아야 함. @ssafy.com은 허용)",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(JsonFieldType.OBJECT).description("에러 상세").optional().ignored()
                                        )
                                )
                                .responseSchema(Schema.schema("Error Response"))
                                .build()
                        ))
                );
    }

    @Test
    void 기업_생성_성공() throws Exception {
        AuthRequest.EnterpriseRegistDto dto = AuthRequest.EnterpriseRegistDto.builder()
                .name("ssafy")
                .industry("제조업")
                .managerEmail("admin@ssafy.com")
                .managerPhoneNumber("010-1234-5678")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        doNothing().when(authService).registEnterprise(any(AuthRequest.EnterpriseRegistDto.class));

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
                                .summary("기업 등록 API")
                                .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("기업명"),
                                        fieldWithPath("industry").type(JsonFieldType.STRING).description("산업분류"),
                                        fieldWithPath("managerEmail").type(JsonFieldType.STRING).description("대표관리자 이메일"),
                                        fieldWithPath("managerPhoneNumber").type(JsonFieldType.STRING).description("대표관리자 전화번호")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").ignored()
                                        )
                                )
                                .requestSchema(Schema.schema("기업 등록 Request"))
                                .build()
                        )));
    }

    @Test
    void 기업_생성_실패_존재하는_기업() throws Exception {
        AuthRequest.EnterpriseRegistDto dto = AuthRequest.EnterpriseRegistDto.builder()
                .name("paori")
                .industry("제조업")
                .managerEmail("admin@paori.com")
                .managerPhoneNumber("010-1234-5678")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        AuthResponse.EnterpriseAccountDto response = AuthResponse.EnterpriseAccountDto.builder()
                .email("pipewatch_admin@paori.com")
                .password("pipewatch1234")
                .build();

        doThrow(new BaseException(DUPLICATED_ENTERPRISE)).when(authService).registEnterprise(any(AuthRequest.EnterpriseRegistDto.class));

        ResultActions actions = mockMvc.perform(
                post("/api/auth/enterprise")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        actions
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.header.httpStatusCode").value(DUPLICATED_ENTERPRISE.getHttpStatusCode()))
                .andExpect(jsonPath("$.header.message").value(DUPLICATED_ENTERPRISE.getMessage()))
                .andDo(document(
                        "기업 등록 실패 - 이미 등록된 기업",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
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
    void 로그인_성공() throws Exception {
        AuthRequest.SigninDto dto = AuthRequest.SigninDto.builder()
                .email("kim@ssafy.com")
                .password("ssafy1234")
                .build();

        String content = objectMapper.writeValueAsString(dto);

        ResultActions actions = mockMvc.perform(
                post("/api/auth/signin")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.httpStatusCode").value(SIGNIN_OK.getHttpStatusCode()))
                .andExpect(jsonPath("$.header.message").value(SIGNIN_OK.getMessage()))
                .andDo(document(
                        "로그인 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
                                .summary("로그인 API")
                                .requestFields(
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("password").description("비밀번호")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").ignored()
                                        )
                                )
                                .requestSchema(Schema.schema("로그인 Request"))
                                .build()
                        )));
    }

    @Test
    void 로그아웃_성공() throws Exception {
        ResultActions actions = mockMvc.perform(
                get("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.httpStatusCode").value(LOGOUT_OK.getHttpStatusCode()))
                .andExpect(jsonPath("$.header.message").value(LOGOUT_OK.getMessage()))
                .andDo(document(
                        "로그아웃 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
                                .summary("로그아웃 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").ignored()
                                        )
                                )
                                .build()
                        )));
    }
}