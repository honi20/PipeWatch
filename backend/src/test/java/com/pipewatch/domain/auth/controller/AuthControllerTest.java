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
import static com.pipewatch.global.statusCode.SuccessCode.ENTERPRISE_CREATED;
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
        AuthDto.EnterpriseRegistRequestDto dto = new AuthDto.EnterpriseRegistRequestDto();
        dto.setName("ssafy");
        dto.setIndustry("제조업");
        dto.setManagerEmail("paori@ssafy.com");
        dto.setManagerPhoneNumber("010-1234-5678");

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
}