package com.pipewatch.domain.enterprise.repository;

import com.pipewatch.domain.enterprise.model.entity.BuildingAndFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BuildingRepository extends JpaRepository<BuildingAndFloor, Long> {
	List<BuildingAndFloor> findByEnterpriseId(Long enterpriseId);

	@Query("SELECT DISTINCT bf.name FROM BuildingAndFloor bf WHERE bf.enterprise.id = :enterpriseId")
	List<String> findDistinctNameByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
}
