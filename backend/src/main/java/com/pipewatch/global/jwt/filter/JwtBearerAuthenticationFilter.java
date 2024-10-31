package com.pipewatch.global.jwt.filter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipewatch.global.jwt.entity.JwtToken;
import com.pipewatch.global.jwt.service.JwtService;
import com.pipewatch.global.redis.RedisUtil;
import com.pipewatch.global.response.ResponseDto;
import com.pipewatch.global.statusCode.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static com.pipewatch.global.statusCode.SuccessCode.ACCESSTOKEN_REISSUED;

@Component
@RequiredArgsConstructor
public class JwtBearerAuthenticationFilter extends GenericFilterBean {
	private final JwtService jwtService;
	private final RedisUtil redisUtils;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String token = extractBearerToken(request);
		if (token == null) {
			filterChain.doFilter(request, response);
			return;
		}
		String token_type = jwtService.getClaim(token);

		if (token_type == null) {
			sendError(response, ErrorCode.INVALID_TOKEN);
			return;
		} else if (token_type.equals("access_token")) {
			if (jwtService.isTokenValid(token)) {
				Authentication auth = jwtService.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
			} else if (jwtService.isTokenExpired(token)) {
				String uuid = jwtService.getUuid(token);
				JwtToken jwtToken = (JwtToken) redisUtils.getData(uuid);
				if (jwtToken != null) {
					String refreshToken = jwtToken.getRefreshToken();
					if (jwtService.isTokenExpired(refreshToken)) {
						redisUtils.deleteData(uuid);
						sendError(response, ErrorCode.EXPIRED_TOKEN);
						return;
					} else {
						sendAccessToken(response, JWT.decode(token).getSubject());
						return;
					}
				}
				sendError(response, ErrorCode.EXPIRED_TOKEN);
				return;
			} else {
				sendError(response, ErrorCode.INVALID_TOKEN);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	public void sendAccessToken(HttpServletResponse response, String uuid) throws IOException {
		if (!response.isCommitted()) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);

			ResponseDto<?> res = ResponseDto.success(ACCESSTOKEN_REISSUED, jwtService.createAccessToken(uuid));
			ObjectMapper mapper = new ObjectMapper();
			response.getWriter().write(mapper.writeValueAsString(res));
		}
	}

	public void sendError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		if (!response.isCommitted()) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(errorCode.getHttpStatusCode());

			ResponseDto<Object> res = ResponseDto.fail(errorCode);
			ObjectMapper mapper = new ObjectMapper();
			response.getWriter().write(mapper.writeValueAsString(res));
		}
	}

	public String extractBearerToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}