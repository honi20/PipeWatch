package com.pipewatch.domain.auth.service;

import com.pipewatch.domain.auth.model.dto.AuthRequest;
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
import com.pipewatch.global.statusCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    @Transactional
    public void signup(AuthRequest.SignupDto requestDto) throws NoSuchAlgorithmException {
        String uuid = UUID.randomUUID().toString();

        User checkUser = userRepository.findByEmail(requestDto.getEmail());
        if (checkUser != null){
            if (checkUser.getState() != State.INACTIVE) {
                throw new BaseException(DUPLICATED_EMAIL);
            }
            else {
                EmployeeInfo employeeInfo = employeeRepository.findByUserId(checkUser.getId());
                employeeRepository.delete(employeeInfo);
                userRepository.delete(checkUser);
                employeeRepository.flush();
                userRepository.flush();
            }
        }

        String password = passwordEncoder.encode(requestDto.getPassword());
        requestDto.setPassword(password);

        Enterprise enterprise = enterpriseRepository.findById(requestDto.getEnterpriseId())
                .orElseThrow(() -> new BaseException(ENTERPRISE_NOT_FOUND));

        // 기업 도메인 메일인지 확인
        if (!getEmailDomain(enterprise.getManagerEmail()).equals(getEmailDomain(requestDto.getEmail()))) {
            throw new BaseException(INVALID_EMAIL_FORMAT);
        }

        // 유저 등록
        User user = userRepository.save(requestDto.toEntity(uuid));

        // 직원 등록
        EmployeeInfo employee = EmployeeInfo.builder()
                .empNo(requestDto.getEmpNo())
                .department(requestDto.getDepartment())
                .empClass(requestDto.getEmpClass())
                .user(user)
                .enterprise(enterprise)
                .build();

        employeeRepository.save(employee);

        // 메일 전송
        String verifyCode = mailService.sendVerifyEmail(requestDto.getEmail());
        redisUtil.setDataWithExpiration("verify_" + verifyCode, requestDto.getEmail(), 600L);
    }

    @Override
    public void verifyEmailCode(String token) {
        String email = redisUtil.getDataByToken("verify_" + token);

        if (email == null) {
            throw new BaseException(VERIFY_NOT_FOUND);
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new BaseException(EMAIL_NOT_FOUND);
        }

        // 이메일 인증 완료 후 Redis에서 토큰 삭제
        redisUtil.deleteData("verify_" + token);

        // 해당 유저의 상태를 pending으로 전환 -> 사원 요청 보냄
        user.updateState(State.PENDING);
        userRepository.save(user);

        Waiting waiting = Waiting.builder().role(Role.ROLE_EMPLOYEE).user(user).build();
        waitingRepository.save(waiting);
    }

    private String getEmailDomain(String email) {
        String[] parts = email.split("@|\\.");
        if (parts.length < 2) {
            throw new BaseException(INVALID_EMAIL_FORMAT);
        }

        return parts[1];
    }
}
