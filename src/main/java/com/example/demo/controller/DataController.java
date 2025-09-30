package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DataController {

    // 메모리에 사용자 저장 (데모용)
    private static List<Map<String, Object>> users = new ArrayList<>();
    private static int nextId = 4;

    static {
        users.add(Map.of(
            "id", 1,
            "name", "김철수",
            "email", "kim@example.com",
            "age", 28
        ));
        users.add(Map.of(
            "id", 2,
            "name", "이영희",
            "email", "lee@example.com",
            "age", 32
        ));
        users.add(Map.of(
            "id", 3,
            "name", "박민수",
            "email", "park@example.com",
            "age", 25
        ));
    }

    @GetMapping("/users")
    public List<Map<String, Object>> getUsers() {
        return new ArrayList<>(users);
    }

    @PostMapping("/users")
    public Map<String, Object> createUser(@RequestBody Map<String, Object> user) {
        // 유효성 검사
        if (!user.containsKey("name") || user.get("name").toString().isBlank()) {
            return Map.of(
                "success", false,
                "message", "이름을 입력해주세요."
            );
        }
        if (!user.containsKey("email") || user.get("email").toString().isBlank()) {
            return Map.of(
                "success", false,
                "message", "이메일을 입력해주세요."
            );
        }
        if (!user.containsKey("age")) {
            return Map.of(
                "success", false,
                "message", "나이를 입력해주세요."
            );
        }

        // 새 사용자 생성
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("id", nextId++);
        newUser.put("name", user.get("name"));
        newUser.put("email", user.get("email"));
        newUser.put("age", user.get("age"));

        users.add(newUser);

        return Map.of(
            "success", true,
            "data", newUser,
            "message", "사용자가 생성되었습니다."
        );
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return Map.of(
            "totalUsers", 150,
            "activeUsers", 98,
            "newToday", 12,
            "timestamp", new Date().toString()
        );
    }
}