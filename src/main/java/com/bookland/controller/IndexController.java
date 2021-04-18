package com.bookland.controller;

import com.bookland.service.OrderDetailService;
import com.bookland.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

import static com.bookland.config.SecurityConfig.PREFIX;


@Controller
@Slf4j
public class IndexController {

    @Autowired
    OrderDetailService orderDetailService;

    @Value("${spring.profiles.active:}")
    String mode;

    @Value("${AWS_S3:}")
    String S3;

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
        model.addAttribute("prefix", mode.equals(PREFIX) ? S3 : "");
        return "bookland_home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = new UserUtil().getUserName(user);
        model.addAttribute("prefix", mode.equals(PREFIX) ? S3 : "");
        model.addAttribute("username", userName);
        return "about";
    }
}
