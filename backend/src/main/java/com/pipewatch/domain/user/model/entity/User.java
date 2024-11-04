package com.pipewatch.domain.user.model.entity;

import com.pipewatch.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Email
	@NotNull
	@Column(unique = true)
	private String email;

	@NotNull
	private String password;

	@NotNull
	private String name;

	@NotNull
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private State state = State.PENDING;

	@NotNull
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private Role role = Role.USER;

	@NotNull
	private String uuid;

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateState(State state) {
		this.state = state;
	}

	public void updateRole(Role role) {
		this.role = role;
	}
}
