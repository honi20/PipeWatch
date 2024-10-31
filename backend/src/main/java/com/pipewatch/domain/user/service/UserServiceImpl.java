package com.pipewatch.domain.user.service;

import com.pipewatch.domain.user.model.dto.UserResponse;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.UserCustomRepository;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.pipewatch.global.statusCode.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	private final UserCustomRepository userCustomRepository;

	@Override
	public UserResponse.MyPageDto getUserDetail(Long userId) {
		// 유저가 존재하는지 확인
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		return userCustomRepository.findUserDetailByUserId(user.getId());
	}
}
