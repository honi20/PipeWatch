package com.pipewatch.domain.user.service;

import com.pipewatch.domain.user.model.dto.UserResponse;

public interface UserService {
	UserResponse.MyPageDto getUserDetail(Long userId);
}
