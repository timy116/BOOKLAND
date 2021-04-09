package com.bookland.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/cart")
public class CartController {

    @GetMapping("/add")
    @ResponseBody
    public String addItemAJAX(HttpServletRequest request, HttpServletResponse response, String id)
            throws JsonProcessingException {
        Cookie cookie;
        Map<String, Integer> cart = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        // 得到 cart cookie
        Optional<Cookie> opt = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("cart"))
                .findAny();

        // 判斷 cookie 是否存在
        if(opt.isPresent()) {
            cookie = opt.get();

            // 得到 cart 內容並轉換回 Map Object
            cart = objectMapper.readValue(cookie.getValue(), new TypeReference<HashMap<String, Integer>>() {});

            // 根據 book id 得到數量
            Integer quantity = cart.get(id);

            // 如果數量不為 null (表示使用者有新增此書本到購物車)，並且數量限制最多只能 5 本
            if (!ObjectUtils.isEmpty(quantity) && quantity < 5) {
                // 數量 +1 並且更新
                cart.put(id, quantity + 1);
            }
            // 如果數量為 null，表示使用者第一次新增該書本到購物車
            else if (ObjectUtils.isEmpty(quantity)) {
                // 設定數量為 1
                cart.put(id, 1);
            }

            cookie.setValue(objectMapper.writeValueAsString(cart));
            cookie.setPath("/");
        } else {
            // 如果 cookie 不存在表示第一次新增書本到購物車，所以會創建一個 Cookie Object

            cart.put(id, 1);
            cookie = new Cookie("cart", objectMapper.writeValueAsString(cart));
            cookie.setPath("/");

            // 設置 Cookie 過期時間(兩周)
            cookie.setMaxAge(3600 * 24 * 14);
        }

        response.addCookie(cookie);
        return "{\"cart\":" + cart.size() + "}";
    }
}
