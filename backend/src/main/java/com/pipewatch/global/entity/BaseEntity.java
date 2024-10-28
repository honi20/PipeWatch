package com.pipewatch.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {
	@Column(updatable = false)
	private LocalDateTime created_at;

	private LocalDateTime updated_at;

	@PrePersist
	public void persist() {
		LocalDateTime now = LocalDateTime.now();
		created_at = now;
		updated_at = now;
	}

	@PreUpdate
	public void preUpdate() {
		updated_at = LocalDateTime.now();
	}
}
