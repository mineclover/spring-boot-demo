package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {

    private final Map<Long, User> users = new ConcurrentHashMap<>();

    public UserRepository() {
        // 초기 데이터
        users.put(1L, new User(1L, "김철수", "kim@example.com", "USER"));
        users.put(2L, new User(2L, "이영희", "lee@example.com", "USER"));
        users.put(3L, new User(3L, "관리자", "admin@example.com", "ADMIN"));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public void deleteById(Long id) {
        users.remove(id);
    }
}