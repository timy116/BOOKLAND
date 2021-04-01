package com.bookland.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@Slf4j
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "bookland_home";
    }
}
