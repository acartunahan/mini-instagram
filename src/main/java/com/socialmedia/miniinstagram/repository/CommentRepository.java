package com.socialmedia.miniinstagram.repository;

import com.socialmedia.miniinstagram.entity.Comment;
import com.socialmedia.miniinstagram.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostOrderByCreatedAtAsc(Post post);

    long countByPost(Post post);
}
