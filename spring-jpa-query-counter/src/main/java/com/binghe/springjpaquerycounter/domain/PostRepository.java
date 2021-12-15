package com.binghe.springjpaquerycounter.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

//    select t from Team t join fetch t.members m
    @Query("select distinct p from Post p join fetch p.comments c")
    public List<Post> findAllFetchJoin();
}
