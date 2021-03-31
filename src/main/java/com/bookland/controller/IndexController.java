package com.bookland.controller;

import com.bookland.entity.Book;
import com.bookland.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class IndexController {

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public List<Book> all() {
        log.debug("Books:" + bookService.all());
        return bookService.all();
    }
}
