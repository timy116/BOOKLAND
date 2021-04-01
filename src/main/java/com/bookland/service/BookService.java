package com.bookland.service;

import com.bookland.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> retrieveAll();
    List<Book> retrieveBooks(String category);
    List<Book> listAllBooksByPage(int pageNum, int pageSize);
    List<Book> listBooksByPage(String category, int pageNum, int pageSize);
}
