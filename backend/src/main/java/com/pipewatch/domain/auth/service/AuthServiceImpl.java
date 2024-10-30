package com.pipewatch.domain.auth.service;

import com.pipewatch.domain.auth.model.dto.AuthRequest;
import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import com.pipewatch.domain.enterprise.repository.EnterpriseRepository;
import com.pipewatch.domain.management.model.entity.Waiting;
import com.pipewatch.domain.management.repository.WaitingRepository;
import com.pipewatch.domain.user.model.entity.EmployeeInfo;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.EmployeeRepository;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import com.pipewatch.global.jwt.entity.JwtToken;
import com.pipewatch.global.jwt.service.JwtService;
import com.pipewatch.global.mail.MailService;
import com.pipewatch.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
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
        redisUtil.setDataWithExpiration(requestDto.getEmail() + "_verify", jwtToken, 1000L);
    }

    @Override
    public void verifyEmailCode(AuthRequest.EmailCodeVerifyDto requestDto) {
        String verify = ((JwtToken) redisUtil.getData(requestDto.getEmail() + "_verify")).getVerify();

        if (verify == null) {
            throw new BaseException(VERIFY_NOT_FOUND);
        }

        if (!verify.equals(requestDto.getVerifyCode())) {
            throw new BaseException(INVALID_EMAIL_CODE);
        }

    }

    @Override
    @Transactional
    public String signup(AuthRequest.SignupDto requestDto) {
        String uuid = UUID.randomUUID().toString();

        if (userRepository.findByEmail(requestDto.getEmail()) != null){
            throw new BaseException(DUPLICATED_EMAIL);
        }

        // 기업 도메인 메일인지 확인
        Enterprise enterprise = enterpriseRepository.findById(requestDto.getEnterpriseId())
                .orElseThrow(() -> new BaseException(ENTERPRISE_NOT_FOUND));

        String domain = getEmailDomain(requestDto.getEmail());
        if (!domain.equals("ssafy.com") && !getEmailDomain(enterprise.getManagerEmail()).equals(domain)) {
            throw new BaseException(INVALID_EMAIL_FORMAT);
        }

        // 이메일 인증 코드 재확인
        String verifyCode = requestDto.getVerifyCode();
        String key = requestDto.getEmail()+"_verify";
        JwtToken token = (JwtToken)redisUtil.getData(key);
        if(token == null) {
            throw new BaseException(SIGNUP_BAD_REQUEST);
        }

        String verify = token.getVerify();
        if(verify == null || !verify.equals(verifyCode)) {
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
                .role(Role.ROLE_EMPLOYEE)
                .user(user)
                .build();

        waitingRepository.save(waiting);

        // jwt 토큰 발급
        JwtToken jwtToken = requestDto.toRedis(uuid, user.getId(), jwtService.createRefreshToken(uuid));
        redisUtil.setData(uuid, jwtToken);

        return jwtService.createAccessToken(uuid);
    }

    private String getEmailDomain(String email) {
        String[] parts = email.split("@");
        if (parts.length < 2) {
            throw new BaseException(INVALID_EMAIL_FORMAT);
        }

        return parts[1];
    }
}
