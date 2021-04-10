package com.bookland.controller;

import com.bookland.entity.Book;
import com.bookland.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    BookService bookService;

    public Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findAny();
    }

    public Map<String, Integer> getCartItems(Cookie cookie) throws JsonProcessingException {
        return new ObjectMapper()
                .readValue(cookie.getValue(), new TypeReference<HashMap<String, Integer>>() {
                });
    }

    @GetMapping("")
    public String cartPage(HttpServletRequest request, Model model) throws JsonProcessingException {
        Optional<Cookie> opt = getCookie(request, "cart");
        List<Integer> idList = new ArrayList<>();

        if (opt.isPresent()) {
            Map<String, Integer> cart = getCartItems(opt.get());
            cart.keySet().forEach(s -> idList.add(Integer.parseInt(s)));
            List<Book> books = bookService.retrieveBooksById(idList);
            log.debug("books: {}", books);
            model.addAttribute("items", cart);
            model.addAttribute("books", books);
        } else {
            model.addAttribute("items", null);
            model.addAttribute("books", null);
        }
        return "cart";
    }

    @GetMapping("/add")
    @ResponseBody
    public String addItemAJAX(HttpServletRequest request, HttpServletResponse response, String id)
            throws JsonProcessingException {
        Cookie cookie;
        Map<String, Integer> cart = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        // 得到 cart cookie
        Optional<Cookie> opt = getCookie(request, "cart");

        // 判斷 cookie 是否存在
        if (opt.isPresent()) {
            cookie = opt.get();

            // 得到 cart 內容並轉換回 Map Object
            cart = getCartItems(cookie);

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

        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return "{\"cart\":" + cart.size() + "}";
    }

    @GetMapping("/remove")
    @ResponseBody
    public String removeItemAJAX(HttpServletRequest request, HttpServletResponse response, String id)
            throws JsonProcessingException {
        Optional<Cookie> opt = getCookie(request, "cart");

        if (opt.isPresent()) {
            Cookie cookie = opt.get();
            Map<String, Integer> cart = getCartItems(cookie);
            cart.remove(id);
            cookie.setValue(new ObjectMapper().writeValueAsString(cart));

            if (cart.size() == 0) {
                cookie = new Cookie("cart", null);

                // 刪除 cookie 必須設定期限為 0
                cookie.setMaxAge(0);
            }
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "{\"cart\":" + cart.size() + "}";
        }
        return "";
    }
}
