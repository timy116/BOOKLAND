package com.bookland.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@Slf4j
public class IndexController {

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        Object obj = request.getSession().getAttribute("checkout");
        if (!ObjectUtils.isEmpty(obj) && (boolean) obj) {
            request.getSession().removeAttribute("checkout");
            return "redirect:/cart";
        }

        return "bookland_home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
