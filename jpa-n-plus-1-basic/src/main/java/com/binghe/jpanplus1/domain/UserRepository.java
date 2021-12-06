package com.binghe.jpanplus1.domain;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    // 아래 쿼리 주석을 해제하면  MultipleBagFetchException이 발생한다.
//    @Query("select u from User u "
//        + "join fetch u.posts "
//        + "join fetch u.comments")
//    List<User> findAllDoubleFetchJoinWithPostAndComment();

    @Query("select distinct u from User u join fetch u.posts")
    List<User> findAllFetchJoinPost();

    @Query("select distinct u from User u")
    List<User> findAll(PageRequest pageRequest);

    @Query("select distinct u from User u join fetch u.posts")
    List<User> findAllWithPost(PageRequest pageRequest);
}
