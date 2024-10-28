package com.pipewatch.global.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime created_at;

	@LastModifiedDate
	private LocalDateTime updated_at;

}
