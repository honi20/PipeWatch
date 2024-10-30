package com.pipewatch.domain.enterprise.repository;

import com.pipewatch.domain.enterprise.model.dto.EnterpriseDto;
import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
    Enterprise findByUserId(Long id);

    @Query("SELECT new com.pipewatch.domain.enterprise.model.dto.EnterpriseDto(e.id, e.name, e.industry) FROM Enterprise e")
    List<EnterpriseDto> findAllEnterprise();
}
