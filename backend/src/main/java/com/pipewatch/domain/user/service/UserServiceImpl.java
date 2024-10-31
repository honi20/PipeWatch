package com.pipewatch.domain.user.service;

import com.pipewatch.domain.user.model.dto.UserRequest;
import com.pipewatch.domain.user.model.dto.UserResponse;
import com.pipewatch.domain.user.model.entity.EmployeeInfo;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.EmployeeRepository;
import com.pipewatch.domain.user.repository.UserCustomRepository;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.pipewatch.global.statusCode.ErrorCode.FORBIDDEN_USER_ROLE;
import static com.pipewatch.global.statusCode.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	private final UserCustomRepository userCustomRepository;
	private final EmployeeRepository employeeRepository;

	private final PasswordEncoder passwordEncoder;

	@Override
	public UserResponse.MyPageDto getUserDetail(Long userId) {
		// 유저가 존재하는지 확인
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		return userCustomRepository.findUserDetailByUserId(user.getId());
	}

	@Override
	@Transactional
	public void modifyUserDetail(Long userId, UserRequest.MyPageModifyDto requestDto) {
		// 유저가 존재하는지 확인
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 기업 유저는 수정 권한 없음
		if (user.getRole() == Role.ROLE_ENTERPRISE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		// 수정
		EmployeeInfo employeeInfo = employeeRepository.findByUserId(user.getId());
		employeeInfo.updateDepartment(requestDto.getDepartment());
		employeeInfo.updateEmpClass(requestDto.getEmpClass());

		employeeRepository.save(employeeInfo);
	}

	@Override
	@Transactional
	public void modifyPassword(Long userId, UserRequest.PasswordModifyDto requestDto) {
		// 유저가 존재하는지 확인
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		String password = passwordEncoder.encode(requestDto.getNewPassword());
		user.updatePassword(password);
		userRepository.save(user);
	}
}
