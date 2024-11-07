package com.pipewatch.domain.user.repository;

import com.pipewatch.domain.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	User findByUuid(String uuid);

	User findByEmailAndName(String email, String name);
}
