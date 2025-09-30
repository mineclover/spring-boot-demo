package com.example.demo.repository;

import com.example.demo.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepository {

    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public PostRepository() {
        // 데모 게시글 초기화
        Post post1 = new Post(idCounter.getAndIncrement(),
            "세션 관리 데모에 오신 것을 환영합니다!",
            "이 페이지에서는 사용자 간 세션 전환과 권한 관리를 테스트할 수 있습니다. 왼쪽 사이드바에서 사용자를 클릭하여 로그인해보세요.",
            3L, "관리자");
        posts.put(post1.getId(), post1);

        try {
            Thread.sleep(1000); // 시간차를 두기 위해
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Post post2 = new Post(idCounter.getAndIncrement(),
            "일반 사용자 권한 테스트",
            "일반 사용자로 로그인하면 자신이 작성한 게시글만 삭제할 수 있습니다.",
            1L, "김철수");
        posts.put(post2.getId(), post2);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Post post3 = new Post(idCounter.getAndIncrement(),
            "관리자 권한 테스트",
            "관리자로 로그인하면 모든 게시글을 삭제할 수 있습니다. 관리자 계정으로 전환해보세요!",
            2L, "이영희");
        posts.put(post3.getId(), post3);
    }

    public List<Post> findAll() {
        return posts.values().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == null) {
            post = new Post(
                idCounter.getAndIncrement(),
                post.getTitle(),
                post.getContent(),
                post.getAuthorId(),
                post.getAuthorName()
            );
        }
        posts.put(post.getId(), post);
        return post;
    }

    public void deleteById(Long id) {
        posts.remove(id);
    }

    public List<Post> findByAuthorId(Long authorId) {
        return posts.values().stream()
                .filter(post -> post.getAuthorId().equals(authorId))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public void restoreDemoPosts() {
        // 기존 게시글 모두 삭제
        posts.clear();
        idCounter.set(1);

        // 데모 게시글 재생성
        Post post1 = new Post(idCounter.getAndIncrement(),
            "세션 관리 데모에 오신 것을 환영합니다!",
            "이 페이지에서는 사용자 간 세션 전환과 권한 관리를 테스트할 수 있습니다. 왼쪽 사이드바에서 사용자를 클릭하여 로그인해보세요.",
            3L, "관리자");
        posts.put(post1.getId(), post1);

        Post post2 = new Post(idCounter.getAndIncrement(),
            "일반 사용자 권한 테스트",
            "일반 사용자로 로그인하면 자신이 작성한 게시글만 삭제할 수 있습니다.",
            1L, "김철수");
        posts.put(post2.getId(), post2);

        Post post3 = new Post(idCounter.getAndIncrement(),
            "관리자 권한 테스트",
            "관리자로 로그인하면 모든 게시글을 삭제할 수 있습니다. 관리자 계정으로 전환해보세요!",
            2L, "이영희");
        posts.put(post3.getId(), post3);
    }
}