package com.example.jpaqueryoptimization.domain.repository;

import com.example.jpaqueryoptimization.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
