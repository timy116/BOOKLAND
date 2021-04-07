package com.bookland.controller;

import com.bookland.entity.Book;
import com.bookland.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/{category}")
    // 依據點選分類顯示書籍
    public String booksPage(Model model, @PathVariable String category) {
        if (category.equals("all")) {
            model.addAttribute("books", bookService.retrieveAll());
        } else {
            model.addAttribute("books", bookService.retrieveBooks(category));
        }

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
            default:
                category = "All";
        }
        model.addAttribute("category", category);
        return "book-cat";
    }

    @GetMapping("/{category}/page{num}")
    @ResponseBody
    // 對書籍分頁
    public List<Book> listBooksByPage(@PathVariable String category, @PathVariable String num, Integer pageSize) {
        int pageNum = Integer.parseInt(num);
        List<Book> books;

        if (pageSize == null) {
            // 一頁最多出現 12 本書
            pageSize = 12;
        }

        if (category.equals("all")) {
            books = bookService.listAllBooksByPage(pageNum, pageSize);
        } else {
            books = bookService.listBooksByPage(category, pageNum, pageSize);
        }
        log.debug("books pagination: " + books);
        return books;
    }

    @GetMapping("/insidepage/{slug}")
    // 書籍內頁
    public String bookDetail(Model model, @PathVariable String slug) {
        model.addAttribute("book", bookService.retrieveBySlug(slug));
        return "insidepage";
    }
}
