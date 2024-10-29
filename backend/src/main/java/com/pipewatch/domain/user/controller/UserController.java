package com.pipewatch.domain.user.controller;

import com.pipewatch.domain.user.model.dto.UserRequestDto;
import com.pipewatch.domain.user.model.dto.UserResponseDto;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.pipewatch.global.statusCode.SuccessCode.*;

@RestController
@RequestMapping("${api_prefix}/user")
@RequiredArgsConstructor
public class UserController {
	@GetMapping("/mypage")
	public ResponseEntity<?> myPage() {
		UserResponseDto.MyPageResponseDto responseDto = UserResponseDto.MyPageResponseDto.builder()
				.name("김싸피")
				.email("kim@ssafy.com")
				.enterpriseName("ssafy")
				.empNo(1123456L)
				.department("경영지원팀")
				.empClass("대리")
				.roll("사원")
				.state("active")
				.build();

		return new ResponseEntity<>(ResponseDto.success(MYPAGE_DETAIL_OK, responseDto), HttpStatus.OK);
	}

	@PutMapping("/mypage")
	public ResponseEntity<?> mypageModify(@RequestBody UserRequestDto.MyPageModifyRequestDto requestDto) {
		return new ResponseEntity<>(ResponseDto.success(MYPAGE_MODIFIED_OK, null), HttpStatus.OK);
	}

	@PatchMapping("/modify-pwd")
	public ResponseEntity<?> passwordModify(@RequestBody UserRequestDto.PasswordModifyRequestDto requestDto) {
		return new ResponseEntity<>(ResponseDto.success(PASSWORD_MODIFIED_OK, null), HttpStatus.OK);
	}

	@DeleteMapping("/withdraw")
	public ResponseEntity<?> withdraw() {
		return new ResponseEntity<>(ResponseDto.success(USER_DELETE_OK, null), HttpStatus.NO_CONTENT);
	}
}
