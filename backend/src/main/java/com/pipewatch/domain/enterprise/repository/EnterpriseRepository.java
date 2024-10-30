package com.pipewatch.domain.enterprise.repository;

import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
}
