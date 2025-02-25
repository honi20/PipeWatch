package com.pipewatch.domain.enterprise.model.entity;

import com.pipewatch.domain.user.model.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Enterprise {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "enterprise_id")
	private Long id;

	@NotNull
	private String name;

	@NotNull
	private String industry;

	@NotNull
	@Column(name = "manager_email")
	@Email(message = "유효한 이메일 형식을 입력해 주세요")
	private String managerEmail;

	@NotNull
	@Column(name = "manager_phone_number")
	private String managerPhoneNumber;

	@Builder.Default
	@Column(name = "is_active")
	private Boolean isActive = true;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public void updateIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
