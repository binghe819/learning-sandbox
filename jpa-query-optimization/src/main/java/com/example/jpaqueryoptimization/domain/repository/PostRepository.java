package com.example.jpaqueryoptimization.domain.repository;

import com.example.jpaqueryoptimization.application.dto.PostWithUserDto;
import com.example.jpaqueryoptimization.domain.Post;
import org.hibernate.annotations.BatchSize;
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

    // 데이터 뻥튀기가 발생한다.
    @Query("select p" +
            " from Post p" +
            " join fetch p.comments c" +
            " join fetch p.writer w" +
            " join fetch w.profile")
    List<Post> findAllWithCommentsFetchJoin();

    @Query("select distinct p" +
            " from Post p" +
            " join fetch p.comments c" +
            " join fetch p.writer w" +
            " join fetch w.profile")
    List<Post> findAllWithCommentsFetchJoinDistinct();

    @Query("select distinct p" +
            " from Post p" +
            " join fetch p.comments c" +
            " join fetch p.writer w" +
            " join fetch w.profile")
    List<Post> findAllWithCommentsFetchJoinDistinctPageable(Pageable pageable);

//    @Query("select distinct p" +
//            " from Post p" +
//            " join fetch p.comments c" +
//            " join fetch p.postTags pt" +
//            " join fetch p.writer w" +
//            " join fetch w.profile wp")
//    List<Post> findAllWithCommentsAndPostTagFetchJoin();

    @Query("select distinct p" +
            " from Post p" +
            " join fetch p.writer w" +
            " join fetch w.profile wp")
    List<Post> findAllWithCommentsAndPostTagWithBatchSizePageable(Pageable pageable);
}
