package com.pipewatch.domain.user.service;

import com.pipewatch.domain.user.model.dto.UserRequest;
import com.pipewatch.domain.user.model.dto.UserResponse;

public interface UserService {
	UserResponse.MyPageDto getUserDetail(Long userId);

	void modifyUserDetail(Long userId, UserRequest.MyPageModifyDto requestDto);

	void modifyPassword(Long userId, UserRequest.PasswordModifyDto requestDto);

	void deleteUser(Long userId);
}
