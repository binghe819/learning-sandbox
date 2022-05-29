package com.example.jpaqueryoptimization.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostTag> postTags = new ArrayList<>();

    @Builder
    public Post(String title, String content, User writer) {
        this(null, title, content, writer, new ArrayList<>(), new ArrayList<>());
    }

    public void addTags(List<Tag> tags) {
        tags.stream()
                .map(tag -> new PostTag(tag, this))
                .forEach(postTag -> this.postTags.add(postTag));
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.updatePost(this);
    }
}
