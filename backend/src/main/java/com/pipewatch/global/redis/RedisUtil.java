package com.pipewatch.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {
	private final RedisTemplate<String, Object> redisTemplate;

	public void setData(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public Object getData(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void deleteData(String key) {
		redisTemplate.delete(key);
	}
}
