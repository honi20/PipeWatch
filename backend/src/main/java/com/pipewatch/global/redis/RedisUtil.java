package com.pipewatch.global.redis;

import com.pipewatch.domain.auth.model.dto.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
	private final RedisTemplate<String, Object> redisTemplate;

	public void setData(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public void setDataWithExpiration(String key, Object value, Long expiredTime) {
		redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.SECONDS);
	}

	public Object getData(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void deleteData(String key) {
		redisTemplate.delete(key);
	}

	public AuthRequest.SignupDto getDataByToken(String token) {
		return (AuthRequest.SignupDto) redisTemplate.opsForValue().get(token);
	}
}
