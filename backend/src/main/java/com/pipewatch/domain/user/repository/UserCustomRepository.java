package com.pipewatch.domain.user.repository;

import com.pipewatch.domain.user.model.dto.UserResponse;

public interface UserCustomRepository {
	UserResponse.MyPageDto findUserDetailByUserId(Long userId);
}
