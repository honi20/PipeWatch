package com.pipewatch.domain.management.repository;

import com.pipewatch.domain.management.model.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
}
