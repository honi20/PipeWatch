package com.pipewatch.domain.pipeline.model.entity;

import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PipeMemo extends BaseEntity {
	@Id
	@GeneratedValue
	@Column(name = "pipe_memo_id")
	private Long id;

	private String meme;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "pipe_id")
	private Pipe pipe;
}
