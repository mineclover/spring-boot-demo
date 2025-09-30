package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class DataController {

    @GetMapping("/users")
    public List<Map<String, Object>> getUsers() {
        List<Map<String, Object>> users = new ArrayList<>();

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

        return users;
    }

    @PostMapping("/users")
    public Map<String, Object> createUser(@RequestBody Map<String, Object> user) {
        user.put("id", new Random().nextInt(1000));
        user.put("created", new Date().toString());
        return Map.of(
            "success", true,
            "data", user,
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