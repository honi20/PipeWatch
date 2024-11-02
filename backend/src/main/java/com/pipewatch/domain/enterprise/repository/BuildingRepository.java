package com.pipewatch.domain.enterprise.repository;

import com.pipewatch.domain.enterprise.model.entity.BuildingAndFloor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildingRepository  extends JpaRepository<BuildingAndFloor, Long> {
    List<BuildingAndFloor> findByEnterpriseId(Long enterpriseId);
}
