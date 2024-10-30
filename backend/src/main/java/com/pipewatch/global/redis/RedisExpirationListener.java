package com.pipewatch.global.redis;

import com.pipewatch.domain.user.model.entity.EmployeeInfo;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.EmployeeRepository;
import com.pipewatch.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisExpirationListener implements MessageListener {

	private final UserRepository userRepository;
	private final RedisUtil redisUtil;
	private final EmployeeRepository employeeRepository;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String expiredKey = message.toString();

		// "verify_:"으로 시작하는 키가 만료될 때 유저 삭제
		if (expiredKey.startsWith("verify_")) {
			String email = expiredKey.split("_", 2)[1];

			// 이메일을 이용하여 사용자와 관련된 정보 조회 및 삭제
			User user = userRepository.findByEmail(email);
			if (user != null) {
				EmployeeInfo employeeInfo = employeeRepository.findByUserId(user.getId());
				if (employeeInfo != null) {
					employeeRepository.delete(employeeInfo);
				}
				userRepository.delete(user);
				employeeRepository.flush();
				userRepository.flush();
			} else {
				log.warn("User with email {} not found for deletion.", email);
			}
		}
	}
}