package com.pipewatch.domain.auth.controller;

import com.pipewatch.domain.auth.model.dto.AuthDto;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pipewatch.global.statusCode.SuccessCode.ENTERPRISE_CREATED;

@RestController
@RequestMapping("${api_prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    @PostMapping("/enterprise")
    public ResponseEntity<?> enterpriseAdd(@RequestBody AuthDto.EnterpriseRegistRequestDto enterpriseRegistRequestDto) {

        return new ResponseEntity<>(ResponseDto.success(ENTERPRISE_CREATED, null), HttpStatus.CREATED);
    }
}