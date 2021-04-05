package com.bookland.controller;

import com.bookland.entity.User;
import com.bookland.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
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
}
