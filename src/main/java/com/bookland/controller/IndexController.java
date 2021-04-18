package com.bookland.controller;

import com.bookland.service.OrderDetailService;
import com.bookland.utils.UserUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@Slf4j
public class IndexController {

    @Autowired
    OrderDetailService orderDetailService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        Object obj = request.getSession().getAttribute("checkout");
        if (!ObjectUtils.isEmpty(obj) && (boolean) obj) {
            request.getSession().removeAttribute("checkout");
            return "redirect:/cart";
        }

        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = new UserUtil().getUserName(user);
        model.addAttribute("username", userName);
        model.addAttribute("details", orderDetailService.retrieveBooksByOrderDetail());
        return "bookland_home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
