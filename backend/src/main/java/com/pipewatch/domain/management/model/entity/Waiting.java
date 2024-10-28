package com.pipewatch.domain.management.model.entity;

import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Waiting {
	@Id
	@GeneratedValue
	@Column(name = "waiting_id")
	private Long id;

	private Role role;

	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
}
