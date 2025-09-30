package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.PostRequest;
import com.example.demo.model.User;
import com.example.demo.model.Post;
import com.example.demo.service.UserService;
import com.example.demo.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/session")
public class SessionController {

    private final UserService userService;
    private final PostService postService;

    public SessionController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }
    
    // 세션 데모 페이지
    @GetMapping
    public String sessionPage() {
        return "session";
    }
    
    // 사용자 목록 조회
    @GetMapping("/users")
    @ResponseBody
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    // 로그인 (세션에 사용자 저장)
    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestBody LoginRequest request, HttpSession session) {
        Optional<User> userOpt = userService.getUserById(request.getUserId());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            return Map.of(
                "success", true,
                "user", user,
                "message", user.getUsername() + "님으로 로그인되었습니다"
            );
        }

        return Map.of("success", false, "message", "사용자를 찾을 수 없습니다");
    }
    
    // 로그아웃
    @PostMapping("/logout")
    @ResponseBody
    public Map<String, Object> logout(HttpSession session) {
        session.invalidate();
        return Map.of("success", true, "message", "로그아웃되었습니다");
    }
    
    // 현재 로그인 사용자 정보
    @GetMapping("/current")
    @ResponseBody
    public Map<String, Object> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            Optional<User> userOpt = userService.getUserById(userId);
            if (userOpt.isPresent()) {
                return Map.of(
                    "loggedIn", true,
                    "user", userOpt.get()
                );
            }
        }

        return Map.of("loggedIn", false);
    }
    
    // 개인 정보 조회 (로그인 필요)
    @GetMapping("/my-info")
    @ResponseBody
    public Map<String, Object> getMyInfo(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return Map.of("error", "로그인이 필요합니다");
        }

        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) {
            return Map.of("error", "사용자를 찾을 수 없습니다");
        }

        User user = userOpt.get();

        return Map.of(
            "user", user,
            "privateData", Map.of(
                "sessionId", session.getId(),
                "loginTime", new Date(session.getCreationTime()),
                "secretInfo", "이 정보는 " + user.getUsername() + "님만 볼 수 있습니다"
            )
        );
    }

    // 게시글 목록 조회
    @GetMapping("/posts")
    @ResponseBody
    public List<Post> getPosts() {
        return postService.getAllPosts();
    }
    
    // 게시글 작성 (로그인 필요)
    @PostMapping("/posts")
    @ResponseBody
    public Map<String, Object> createPost(@RequestBody PostRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");

        if (userId == null) {
            return Map.of("success", false, "message", "로그인이 필요합니다");
        }

        Post post = postService.createPost(request.getTitle(), request.getContent(), userId, username);

        return Map.of(
            "success", true,
            "post", post,
            "message", "게시글이 작성되었습니다"
        );
    }

    // 게시글 삭제 (본인 또는 관리자만 가능)
    @DeleteMapping("/posts/{id}")
    @ResponseBody
    public Map<String, Object> deletePost(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null) {
            return Map.of("success", false, "message", "로그인이 필요합니다");
        }

        try {
            postService.deletePost(id, userId, role);
            return Map.of("success", true, "message", "게시글이 삭제되었습니다");
        } catch (IllegalArgumentException e) {
            return Map.of("success", false, "message", e.getMessage());
        } catch (IllegalStateException e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    // 데모 게시글 복원 (관리자만 가능)
    @PostMapping("/posts/restore-demo")
    @ResponseBody
    public Map<String, Object> restoreDemoPosts(HttpSession session) {
        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            return Map.of("success", false, "message", "관리자만 복원할 수 있습니다");
        }

        postService.restoreDemoPosts();
        return Map.of("success", true, "message", "데모 게시글이 복원되었습니다");
    }
}
