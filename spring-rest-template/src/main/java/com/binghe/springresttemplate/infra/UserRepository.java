package com.binghe.springresttemplate.infra;

import com.binghe.springresttemplate.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
