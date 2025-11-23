package com.socialmedia.miniinstagram.service;

import com.socialmedia.miniinstagram.dto.PostCreateRequest;
import com.socialmedia.miniinstagram.dto.PostUpdateRequest;
import com.socialmedia.miniinstagram.entity.Post;
import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.model.Role;
import com.socialmedia.miniinstagram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public List<Post> getAll() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Post updatePost(Long id, User currentUser, PostUpdateRequest req) {
        Post post = getById(id);

        if (!isOwnerOrAdmin(post, currentUser)) {
            throw new RuntimeException("You are not allowed to update this post");
        }

        post.setImageUrl(req.getImageUrl());
        post.setDescription(req.getDescription());

        return postRepository.save(post);
    }

    public void deletePost(Long id, User currentUser) {
        Post post = getById(id);

        if (!isOwnerOrAdmin(post, currentUser)) {
            throw new RuntimeException("You are not allowed to delete this post");
        }

        postRepository.delete(post);
    }

    public void incrementViewCount(Long id) {
        Post post = getById(id);
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
    }

    private boolean isOwnerOrAdmin(Post post, User currentUser) {
        boolean owner = post.getUser().getId().equals(currentUser.getId());
        boolean admin = currentUser.getRole() == Role.ADMIN;
        return owner || admin;
    }
}
