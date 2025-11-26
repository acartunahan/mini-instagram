package com.socialmedia.miniinstagram.service;

import com.socialmedia.miniinstagram.dto.PostCreateRequest;
import com.socialmedia.miniinstagram.dto.PostUpdateRequest;
import com.socialmedia.miniinstagram.entity.Post;
import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.exception.AppException;
import com.socialmedia.miniinstagram.model.Role;
import com.socialmedia.miniinstagram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post createPost(User currentUser, PostCreateRequest req) {

        Post post = new Post();
        post.setUser(currentUser);
        post.setImageUrl(req.getImageUrl());
        post.setDescription(req.getDescription());

        return postRepository.save(post);
    }

    public Post getById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new AppException("Post not found", HttpStatus.NOT_FOUND));
    }

    public List<Post> getAll() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Post updatePost(Long id, User currentUser, PostUpdateRequest req) {

        Post post = getById(id);

        if (!isOwnerOrAdmin(post, currentUser)) {
            throw new AppException("You are not allowed to update this post", HttpStatus.FORBIDDEN);
        }

        if (req.getImageUrl() != null && !req.getImageUrl().isBlank()) {
            post.setImageUrl(req.getImageUrl());
        }

        if (req.getDescription() != null && !req.getDescription().isBlank()) {
            post.setDescription(req.getDescription());
        }

        return postRepository.save(post);
    }

    public void deletePost(Long id, User currentUser) {

        Post post = getById(id);

        if (!isOwnerOrAdmin(post, currentUser)) {
            throw new AppException("You are not allowed to delete this post", HttpStatus.FORBIDDEN);
        }

        postRepository.delete(post);
    }

    public void incrementViewCount(Long id) {
        Post post = getById(id);
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
    }

    private boolean isOwnerOrAdmin(Post post, User currentUser) {
        return post.getUser().getId().equals(currentUser.getId()) ||
                currentUser.getRole() == Role.ADMIN;
    }
}
