package com.bookland.controller;

import com.bookland.entity.User;
import com.bookland.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage(String error, Model model) {
        if (error == null) {
            model.addAttribute("error", false);
        } else {
            model.addAttribute("error", true);
        }

        return "login";
    }

    @PostMapping("/login")
    public String login() {
        return "/";
    }

    @GetMapping("/inspect")
    @ResponseBody
    // 檢查帳號與 email 是否重複
    public Map<String, Integer> userInspect(String username, String email) {
        Map<String, Integer> status = new HashMap<>();
        User user = userService.retrieveByUserName(username);

        if (!ObjectUtils.isEmpty(user))
            status.put("u", 1);

        user = userService.retrieveByEmail(email);
        if (!ObjectUtils.isEmpty(user))
            status.put("e", 1);
        return status;
    }

    @PostMapping("/register")
    public String register(HttpServletRequest request) {
        User user = new User();
        user.setUserName(request.getParameter("username"));
        user.setPassword(passwordEncoder.encode(request.getParameter("password")));
        user.setEmail(request.getParameter("email"));
        userService.create(user);
        return "redirect:login";
    }
}
