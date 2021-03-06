package com.bookland.controller;

import com.bookland.entity.Book;
import com.bookland.entity.User;
import com.bookland.service.BookService;
import com.bookland.service.UserService;
import com.bookland.utils.UserUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.bookland.config.SecurityConfig.PREFIX;
import static com.bookland.utils.CookieUtil.getCartItems;
import static com.bookland.utils.CookieUtil.getCookie;

@Controller
@Slf4j
@RequestMapping("/cart")
public class CartController {

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @Value("${spring.profiles.active:}")
    String mode;

    @Value("${AWS_S3:}")
    String S3;

    @GetMapping("")
    public String cartPage(HttpServletRequest request, Model model) throws JsonProcessingException {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int total = 0;

        if (!obj.equals("anonymousUser")) {
            String userName = new UserUtil().getUserName(obj);
            User user = userService.retrieveByUserName(userName);
            model.addAttribute("user", user);
        }
        Cookie cookie = getCookie(request, "cart");
        List<Integer> idList = new ArrayList<>();

        if (!ObjectUtils.isEmpty(cookie)) {
            Map<String, Integer> cart = getCartItems(cookie);
            cart.keySet().forEach(s -> idList.add(Integer.parseInt(s)));
            List<Book> books = bookService.retrieveBooksById(idList);
            if(books.size() != 0)
                total = books.stream().mapToInt(book -> book.getPrice() * cart.get(Integer.toString(book.getId()))).sum();

            model.addAttribute("total", total);
            model.addAttribute("items", cart);
            model.addAttribute("books", books);
        } else {
            model.addAttribute("total", total);
            model.addAttribute("items", null);
            model.addAttribute("books", null);
        }
        model.addAttribute("prefix", mode.equals(PREFIX) ? S3 : "");
        return "cart";
    }

    @GetMapping("/add")
    @ResponseBody
    public String addItemAJAX(HttpServletRequest request, HttpServletResponse response, String id)
            throws JsonProcessingException {
        Cookie cookie = getCookie(request, "cart");
        Map<String, Integer> cart = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        if (!ObjectUtils.isEmpty(cookie)) {
            // ?????? cart ?????????????????? Map Object
            cart = getCartItems(cookie);

            // ?????? book id ????????????
            Integer quantity = cart.get(id);

            // ?????????????????? null (?????????????????????????????????????????????)????????????????????????????????? 5 ???
            if (!ObjectUtils.isEmpty(quantity) && quantity < 5) {
                // ?????? +1 ????????????
                cart.put(id, quantity + 1);
            }
            // ??????????????? null??????????????????????????????????????????????????????
            else if (ObjectUtils.isEmpty(quantity)) {
                // ??????????????? 1
                cart.put(id, 1);
            }

            cookie.setValue(objectMapper.writeValueAsString(cart));
            cookie.setPath("/");
        } else {
            // ?????? cookie ???????????????????????????????????????????????????????????????????????? Cookie Object

            cart.put(id, 1);
            cookie = new Cookie("cart", objectMapper.writeValueAsString(cart));
            cookie.setPath("/");

            // ?????? Cookie ????????????(??????)
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
        Cookie cookie = getCookie(request, "cart");

        if (!ObjectUtils.isEmpty(cookie)) {
            Map<String, Integer> cart = getCartItems(cookie);
            cart.remove(id);
            cookie.setValue(new ObjectMapper().writeValueAsString(cart));

            if (cart.size() == 0) {
                cookie = new Cookie("cart", null);

                // ?????? cookie ????????????????????? 0
                cookie.setMaxAge(0);
            }
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "{\"cart\":" + cart.size() + "}";
        }
        return "";
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateItemAJAX(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody Map<String, Integer> params) throws JsonProcessingException {
        Cookie cookie = getCookie(request, "cart");

        if (!ObjectUtils.isEmpty(cookie)) {
            Integer qty;
            qty = params.get("qty") > 5 ? new Integer(5) : params.get("qty");
            Map<String, Integer> cart = getCartItems(cookie);
            cart.put(params.get("id").toString(), qty);
            cookie.setValue(new ObjectMapper().writeValueAsString(cart));
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "{\"cart\":" + cart.size() + "}";
        }
        return "";
    }
}
