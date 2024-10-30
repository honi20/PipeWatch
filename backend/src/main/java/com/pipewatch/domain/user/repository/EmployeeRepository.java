package com.pipewatch.domain.user.repository;

import com.pipewatch.domain.user.model.entity.EmployeeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeInfo, Long>  {
}
