package com.socialmedia.miniinstagram.service;

import com.socialmedia.miniinstagram.entity.Like;
import com.socialmedia.miniinstagram.entity.Post;
import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.exception.AppException;
import com.socialmedia.miniinstagram.repository.LikeRepository;
import com.socialmedia.miniinstagram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Transactional
    public int likePost(Long postId, User currentUser) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException("Post not found", HttpStatus.NOT_FOUND));

        if (likeRepository.findByPostAndUser(post, currentUser).isPresent()) {
            return post.getLikeCount();
        }

        Like like = new Like();
        like.setPost(post);
        like.setUser(currentUser);
        likeRepository.save(like);

        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);

        return post.getLikeCount();
    }

    @Transactional
    public int unlikePost(Long postId, User currentUser) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException("Post not found", HttpStatus.NOT_FOUND));

        Like like = likeRepository.findByPostAndUser(post, currentUser)
                .orElseThrow(() -> new AppException("Like not found", HttpStatus.NOT_FOUND));

        likeRepository.delete(like);

        int newCount = Math.max(0, post.getLikeCount() - 1);
        post.setLikeCount(newCount);
        postRepository.save(post);

        return newCount;
    }
}
