package com.bookland.controller;

import com.bookland.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private BookService bookService;

    @GetMapping("")
    // 依據點選分類顯示書籍
    public String searchPage(Model model, String keyword) {
        if (keyword.equals(""))
            return "redirect:/book/all";

        model.addAttribute("books", bookService.searchBook(keyword));
        model.addAttribute("keyword", keyword);
        return "book-cat";
    }
}
