package com.binghe.springresttemplate.user.infra;

import com.binghe.springresttemplate.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
