package com.example.jpaqueryoptimization.domain.repository;

import com.example.jpaqueryoptimization.application.dto.PostWithUserDto;
import com.example.jpaqueryoptimization.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p" +
            " from Post p" +
            " join fetch p.writer w")
    List<Post> findAllWithUser();

    @Query("select p" +
            " from Post p" +
            " join fetch p.writer w" +
            " join fetch w.profile")
    List<Post> findAllWithUserAndProfile();

    @Query("select p" +
            " from Post p" +
            " join fetch p.writer w" +
            " join fetch w.profile")
    List<Post> findAllPosts(Pageable pageable);

    @Query("select new com.example.jpaqueryoptimization.application.dto.PostWithUserDto(p.id, p.title, p.content, w.id, w.name, wp.address, wp.phoneNumber)" +
            " from Post p" +
            " join p.writer w" +
            " join w.profile wp")
    List<PostWithUserDto> findPostWithUserDtos();
}
