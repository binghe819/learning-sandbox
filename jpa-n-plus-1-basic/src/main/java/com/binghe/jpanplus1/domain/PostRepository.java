package com.binghe.jpanplus1.domain;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch p.comments")
    List<Post> findAllFetchJoin();

    @EntityGraph(attributePaths = "comments")
    @Query("select p from Post p")
    List<Post> findAllEntityGraph();

    @Query("select DISTINCT p from Post p join fetch p.comments")
    List<Post> findAllFetchJoinDistinct();

    @EntityGraph(attributePaths = "comments")
    @Query("select DISTINCT p from Post p")
    List<Post> findAllEntityGraphDistinct();
}
