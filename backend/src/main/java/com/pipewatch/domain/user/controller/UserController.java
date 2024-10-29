package com.pipewatch.domain.user.controller;

import com.pipewatch.domain.user.model.dto.UserResponseDto;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pipewatch.global.statusCode.SuccessCode.MYPAGE_OK;

@RestController
@RequestMapping("${api_prefix}/user")
@RequiredArgsConstructor
public class UserController {
	@GetMapping("/mypage")
	public ResponseEntity<?> myPage() {
		UserResponseDto.MyPageResponseDto response = UserResponseDto.MyPageResponseDto.builder()
				.name("김싸피")
				.email("kim@ssafy.com")
				.enterpriseName("ssafy")
				.empNo(1123456L)
				.department("경영지원팀")
				.empClass("대리")
				.roll("사원")
				.state("active")
				.build();

		return new ResponseEntity<>(ResponseDto.success(MYPAGE_OK, response), HttpStatus.OK);
	}
}
