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

		// "verifyToken:"으로 시작하는 키가 만료될 때 유저 삭제
		if (expiredKey.startsWith("verify_")) {
			String token = expiredKey.split("_")[1];
			String email = redisUtil.getDataByToken(token);

			User user = userRepository.findByEmail(email);
			EmployeeInfo employeeInfo = employeeRepository.findByUserId(user.getId());

			employeeRepository.delete(employeeInfo);
			userRepository.delete(user);
		}
	}
}