package com.bookland.controller;

import com.bookland.entity.Book;
import com.bookland.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@Slf4j
public class IndexController {

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String index() {
        log.debug("Books:" + bookService.all());
        return "bookland_home";
    }
}
