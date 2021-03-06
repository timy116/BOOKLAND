package com.bookland.controller;

import com.bookland.config.SecurityConfig;
import com.bookland.entity.Book;
import com.bookland.service.BookService;
import com.bookland.utils.UserUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bookland.config.SecurityConfig.PREFIX;

@Controller
@Slf4j
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Value("${spring.profiles.active}")
    String mode;

    @Value("${AWS_S3}")
    String S3;

    @GetMapping("/{category}")
    // 依據點選分類顯示書籍
    public String booksPage(Model model, @PathVariable String category) {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = new UserUtil().getUserName(user);

        List<Book> books;

        if (category.equals("all"))
            books = bookService.retrieveAll();
        else if (category.equals("new")) {
            books = bookService.retrieveNewBooks();
        }
        else
            books = bookService.retrieveBooks(category);

        model.addAttribute("books", books);

        switch (category) {
            case "fine-art":
                category = "Fine Art";
                break;
            case "photography":
                category = "Photography";
                break;
            case "poster-design":
                category = "Poster & Design";
                break;
            case "magazine":
                category = "Magazine";
                break;
            case "new":
                category = "new";
                break;
            default:
                category = "All";
        }
        model.addAttribute("category", category);
        model.addAttribute("prefix", mode.equals(PREFIX) ? S3 : "");
        model.addAttribute("username", userName);
        return "book-cat";
    }

    @GetMapping("/{category}/page{num}")
    @ResponseBody
    // 對書籍分頁
    public String listBooksByPage(@PathVariable String category, @PathVariable String num, Integer pageSize) throws JsonProcessingException {
        int pageNum = Integer.parseInt(num);
        List<Book> books;
        List<Object> objects = new ArrayList<>();

        if (pageSize == null) {
            // 一頁最多出現 12 本書
            pageSize = 12;
        }

        if (category.equals("all")) {
            books = bookService.listAllBooksByPage(pageNum, pageSize);
        } else {
            books = bookService.listBooksByPage(category, pageNum, pageSize);
        }
        books.forEach(book -> {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", book.getId());
            obj.put("name", book.getName());
            obj.put("slug", book.getSlug());
            obj.put("price", book.getPrice());
            obj.put("imageUrl",  (mode.equals(PREFIX) ? S3 : "") + book.getImageUrl());
            objects.add(obj);
        });

        return new ObjectMapper().writeValueAsString(objects);
    }

    @GetMapping("/insidepage/{slug}")
    // 書籍內頁
    public String bookDetail(Model model, @PathVariable String slug) {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = new UserUtil().getUserName(user);

        model.addAttribute("book", bookService.retrieveBySlug(slug));
        model.addAttribute("prefix", mode.equals(PREFIX) ? S3 : "");
        model.addAttribute("username", userName);
        return "insidepage";
    }
}
