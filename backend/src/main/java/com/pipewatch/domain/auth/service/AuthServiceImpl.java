package com.pipewatch.domain.auth.service;

import com.pipewatch.domain.auth.model.dto.AuthRequest;
import com.pipewatch.domain.auth.model.dto.AuthResponse;
import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import com.pipewatch.domain.enterprise.repository.EnterpriseRepository;
import com.pipewatch.domain.management.model.entity.Waiting;
import com.pipewatch.domain.management.repository.WaitingRepository;
import com.pipewatch.domain.user.model.entity.EmployeeInfo;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.State;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.EmployeeRepository;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import com.pipewatch.global.jwt.entity.JwtToken;
import com.pipewatch.global.jwt.service.JwtService;
import com.pipewatch.global.mail.MailService;
import com.pipewatch.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

import static com.pipewatch.global.statusCode.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;

	private final RedisUtil redisUtil;

	private final MailService mailService;

	private final JwtService jwtService;

	private final PasswordEncoder passwordEncoder;

	private final WaitingRepository waitingRepository;
	private final EnterpriseRepository enterpriseRepository;
	private final EmployeeRepository employeeRepository;

	@Override
	public void sendEmailCode(AuthRequest.EmailCodeSendDto requestDto) throws NoSuchAlgorithmException {
		User user = userRepository.findByEmail(requestDto.getEmail());

		if (user != null) {
			throw new BaseException(DUPLICATED_EMAIL);
		}

		String verifyCode = mailService.sendVerifyEmail(requestDto.getEmail());

		JwtToken jwtToken = JwtToken.builder().verify(verifyCode).build();
		redisUtil.setDataWithExpiration(requestDto.getEmail() + "_verify", jwtToken, 900L);
	}

	@Override
	public void verifyEmailCode(AuthRequest.EmailCodeVerifyDto requestDto) {
		JwtToken token = ((JwtToken) redisUtil.getData(requestDto.getEmail() + "_verify"));
		if (token == null) {
			throw new BaseException(VERIFY_NOT_FOUND);
		}

		String verify = token.getVerify();
		if (verify == null) {
			throw new BaseException(VERIFY_NOT_FOUND);
		}

		if (!verify.equals(requestDto.getVerifyCode())) {
			throw new BaseException(INVALID_EMAIL_CODE);
		}

	}

	@Override
	@Transactional
	public AuthResponse.AccessTokenDto signup(AuthRequest.SignupDto requestDto) {
		String uuid = UUID.randomUUID().toString();

		if (userRepository.findByEmail(requestDto.getEmail()) != null) {
			throw new BaseException(DUPLICATED_EMAIL);
		}

		// 기업 도메인 메일인지 확인
		Enterprise enterprise = enterpriseRepository.findById(requestDto.getEnterpriseId())
				.orElseThrow(() -> new BaseException(ENTERPRISE_NOT_FOUND));

		// 서비스 시연을 위해 gmail/naver 등의 메일 형식은 ssafy 기업이라고 가정
		String domain = getEmailDomain(requestDto.getEmail());
		domain = isEnterpriseDomain(domain) ? domain : "paori.com";
		if (!getEmailDomain(enterprise.getUser().getEmail()).equals(domain)) {
			throw new BaseException(INVALID_EMAIL_FORMAT);
		}

		// 이메일 인증 코드 재확인
		String verifyCode = requestDto.getVerifyCode();
		String key = requestDto.getEmail() + "_verify";
		JwtToken token = (JwtToken) redisUtil.getData(key);
		if (token == null) {
			throw new BaseException(SIGNUP_BAD_REQUEST);
		}

		String verify = token.getVerify();
		if (verify == null || !verify.equals(verifyCode)) {
			throw new BaseException(SIGNUP_BAD_REQUEST);
		}
		redisUtil.deleteData(key);

		// 비밀번호 암호화
		String password = passwordEncoder.encode(requestDto.getPassword());
		requestDto.setPassword(password);

		// 유저 저장
		User user = userRepository.save(requestDto.toEntity(uuid));

		// 직원 정보 저장
		EmployeeInfo employee = EmployeeInfo.builder()
				.empNo(requestDto.getEmpNo())
				.department(requestDto.getDepartment())
				.empClass(requestDto.getEmpClass())
				.user(user)
				.enterprise(enterprise)
				.build();

		employeeRepository.save(employee);

		// 승인대기 저장
		Waiting waiting = Waiting.builder()
				.role(Role.EMPLOYEE)
				.user(user)
				.build();

		waitingRepository.save(waiting);

		// jwt 토큰 발급
		JwtToken jwtToken = requestDto.toRedis(uuid, user.getId(), jwtService.createRefreshToken(uuid));
		redisUtil.setData(uuid, jwtToken);

		return AuthResponse.AccessTokenDto.builder()
				.accessToken(jwtService.createAccessToken(uuid))
				.build();
	}

	@Override
	@Transactional
	public void registEnterprise(AuthRequest.EnterpriseRegistDto requestDto) throws NoSuchAlgorithmException {
		String domain = getEmailDomain(requestDto.getManagerEmail());

		// 서비스 시연을 위해 gmail/naver 등의 메일 형식은 ssafy 기업이라고 가정
		domain = isEnterpriseDomain(domain) ? domain : "paori.com";
		String email = "pipewatch_admin@" + domain;

		String password = "pipewatch" + generateRandomNumber() + "!";
		String passwordEncode = passwordEncoder.encode(password);
		String uuid = UUID.randomUUID().toString();

		// 이미 등록된 기업인지 확인
		if (userRepository.findByEmail(email) != null) {
			throw new BaseException(DUPLICATED_ENTERPRISE);
		}

		// 유저 저장
		User user = User.builder()
				.email(email)
				.password(passwordEncode)
				.name(requestDto.getName())
				.state(State.ACTIVE)
				.role(Role.ENTERPRISE)
				.uuid(uuid)
				.build();

		userRepository.save(user);

		// 기업 저장
		enterpriseRepository.save(requestDto.toEntity(user));

		// 메일 전송
		mailService.sendEnterpriseAccountEmail(requestDto.getManagerEmail(), email, password);
	}

	@Override
	public AuthResponse.AccessTokenDto signin(AuthRequest.SigninDto requestDto) {
		User user = userRepository.findByEmail(requestDto.getEmail());

		if (user == null) {
			throw new BaseException(EMAIL_NOT_FOUND);
		}

		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new BaseException(INVALID_PASSWORD);
		}

		JwtToken jwtToken = JwtToken.builder()
				.userId(user.getId())
				.uuid(user.getUuid())
				.refreshToken(jwtService.createRefreshToken(user.getUuid()))
				.build();

		redisUtil.setData(user.getUuid(), jwtToken);

		return AuthResponse.AccessTokenDto.builder()
				.accessToken(jwtService.createAccessToken(user.getUuid()))
				.build();
	}

	@Override
	@Transactional
	public void logout(Long userId) {
		User user = userRepository.findById(userId)
						.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		String userUuid = user.getUuid();

		redisUtil.deleteData(userUuid);
	}

	@Override
	public void sendPasswordResetEmail(AuthRequest.EmailPwdSendDto requestDto) {
		User user = userRepository.findByEmailAndName(requestDto.getEmail(), requestDto.getName());
		if (user == null) {
			throw new BaseException(USER_NOT_FOUND);
		}

		// 메일 전송
		String pwdUuid = mailService.sendPasswordResetEmail(requestDto.getEmail());

		redisUtil.setDataWithExpiration(pwdUuid + "_pwdUuid", requestDto.getEmail(), 900L);
	}

	@Override
	@Transactional
	public void resetPassword(AuthRequest.PasswordResetDto requestDto) {
		// pwdUuid를 통해 유저 이메일 찾기
		String email = redisUtil.getDataByPwdUuid(requestDto.getPwdUuid() + "_pwdUuid");

		if (email == null) {
			throw new BaseException(VERIFY_NOT_FOUND);
		}

		// 유저 비밀번호 변경
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new BaseException(EMAIL_NOT_FOUND);
		}

		String password = passwordEncoder.encode(requestDto.getNewPassword());
		user.updatePassword(password);
		userRepository.save(user);

		redisUtil.deleteData(requestDto.getPwdUuid() + "_pwdUuid");
	}

	private boolean isEnterpriseDomain(String domain) {
		return !domain.equals("gmail.com") && !domain.equals("naver.com");
	}

	private String generateRandomNumber() throws NoSuchAlgorithmException {
		int lenth = 6;
		Random random = SecureRandom.getInstanceStrong();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < lenth; i++) {
			builder.append(random.nextInt(10));
		}
		return builder.toString();
	}

	private String getEmailDomain(String email) {
		String[] parts = email.split("@");
		if (parts.length < 2) {
			throw new BaseException(INVALID_EMAIL_FORMAT);
		}

		return parts[1];
	}
}
