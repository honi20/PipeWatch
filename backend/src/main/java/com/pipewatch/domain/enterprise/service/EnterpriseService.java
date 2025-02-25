package com.pipewatch.domain.enterprise.service;

import com.pipewatch.domain.enterprise.model.dto.EnterpriseResponse;

public interface EnterpriseService {
	EnterpriseResponse.DetailDto getEnterpriseDetail(Long userId);

	EnterpriseResponse.ListDto getEnterpriseList();

	EnterpriseResponse.BuildingListDto getBuildingList(Long userId);

	EnterpriseResponse.BuildingAndFloorListDto getBuildingAndFloorList(Long userId);
}
