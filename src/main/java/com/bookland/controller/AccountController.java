package com.bookland.controller;

import com.bookland.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class AccountController {
    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String accountInformation(String username){
        return "account";
    }

    @PostMapping("/accountUpdate")
    public String accountUpdete(String userName,String name,String email,String phone,String address){
        userService.updateUser(userName,name,email,phone,address);
        return "account";
    }
}
