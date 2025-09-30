package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ComponentController {

    private final UserService userService;

    public ComponentController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/components")
    public String componentsDemo(Model model) {
        // 사용자 데이터를 모델에 추가하여 User Card 컴포넌트 데모에 사용
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "components-demo";
    }
}