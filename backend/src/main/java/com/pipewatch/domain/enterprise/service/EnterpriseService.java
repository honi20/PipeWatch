package com.pipewatch.domain.enterprise.service;

import com.pipewatch.domain.enterprise.model.dto.EnterpriseResponse;

public interface EnterpriseService {
    EnterpriseResponse.DetailDto getEnterpriseDetail();

    EnterpriseResponse.ListDto getEnterpriseList();
}
