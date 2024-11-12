package com.pipewatch.domain.user.controller;

import com.pipewatch.domain.user.model.dto.UserRequest;
import com.pipewatch.domain.user.model.dto.UserResponse;
import com.pipewatch.domain.user.service.UserService;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.pipewatch.global.statusCode.SuccessCode.*;

@RestController
@RequestMapping("${api_prefix}/users")
@RequiredArgsConstructor
public class UserController implements UserApiSwagger {
	private final UserService userService;

	@GetMapping("/mypage")
	public ResponseEntity<?> myPage(@AuthenticationPrincipal Long userId) {
		UserResponse.MyPageDto responseDto = userService.getUserDetail(userId);

		return new ResponseEntity<>(ResponseDto.success(MYPAGE_DETAIL_OK, responseDto), HttpStatus.OK);
	}

	@PutMapping("/mypage")
	public ResponseEntity<?> mypageModify(@AuthenticationPrincipal Long userId, @RequestBody UserRequest.MyPageModifyDto requestDto) {
		userService.modifyUserDetail(userId, requestDto);

		return new ResponseEntity<>(ResponseDto.success(MYPAGE_MODIFIED_OK, null), HttpStatus.OK);
	}

	@PatchMapping("/modify-pwd")
	public ResponseEntity<?> passwordModify(@AuthenticationPrincipal Long userId, @RequestBody UserRequest.PasswordModifyDto requestDto) {
		userService.modifyPassword(userId, requestDto);

		return new ResponseEntity<>(ResponseDto.success(PASSWORD_MODIFIED_OK, null), HttpStatus.OK);
	}

	@PatchMapping("/request")
	public ResponseEntity<?> requestAssign(@AuthenticationPrincipal Long userId) {
		userService.modifyUserState(userId);

		return new ResponseEntity<>(ResponseDto.success(ROLE_ASSIGN_REQUEST_OK, null), HttpStatus.OK);
	}

	@DeleteMapping("/withdraw")
	public ResponseEntity<?> withdraw(@AuthenticationPrincipal Long userId, @RequestBody UserRequest.WithdrawDto requestDto) {
		userService.deleteUser(userId, requestDto);

		return new ResponseEntity<>(ResponseDto.success(USER_DELETE_OK, null), HttpStatus.NO_CONTENT);
	}
}
