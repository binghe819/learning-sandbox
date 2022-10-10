package com.binghe.springdocopenapiswagger.user.infra;

import com.binghe.springdocopenapiswagger.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
