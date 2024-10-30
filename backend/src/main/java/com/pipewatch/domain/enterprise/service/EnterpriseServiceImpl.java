package com.pipewatch.domain.enterprise.service;

import com.pipewatch.domain.enterprise.model.dto.EnterpriseResponse;
import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import com.pipewatch.domain.enterprise.repository.EnterpriseRepository;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import com.pipewatch.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.pipewatch.global.statusCode.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EnterpriseServiceImpl implements EnterpriseService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;

    @Override
    public EnterpriseResponse.DetailDto detailEnterprise() {
        Long userId = jwtService.getUserId(SecurityContextHolder.getContext());
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));

        Enterprise enterprise = enterpriseRepository.findByUserId(user.getId());

        if (enterprise == null) {
            throw new BaseException(ENTERPRISE_NOT_FOUND);
        }

        return EnterpriseResponse.DetailDto.toDto(enterprise);
    }
}
