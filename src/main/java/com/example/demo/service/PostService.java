package com.example.demo.service;

import com.example.demo.model.Post;
import com.example.demo.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post createPost(String title, String content, Long authorId, String authorName) {
        Post post = new Post(null, title, content, authorId, authorName);
        return postRepository.save(post);
    }

    public void deletePost(Long postId, Long userId, String userRole) {
        Optional<Post> postOpt = postRepository.findById(postId);

        if (postOpt.isEmpty()) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다");
        }

        Post post = postOpt.get();

        // 본인이거나 관리자인 경우 삭제 가능
        if (post.getAuthorId().equals(userId) || "ADMIN".equals(userRole)) {
            postRepository.deleteById(postId);
        } else {
            throw new IllegalStateException("삭제 권한이 없습니다");
        }
    }

    public List<Post> getPostsByAuthor(Long authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    public void restoreDemoPosts() {
        postRepository.restoreDemoPosts();
    }
}