package com.bookland.service;

import com.bookland.entity.Book;

import java.util.List;

public interface BookService {
    Book retrieveBySlug(String slug);
    List<Book> retrieveAll();
    List<Book> retrieveBooks(String category);
    List<Book> listAllBooksByPage(int pageNum, int pageSize);
    List<Book> listBooksByPage(String category, int pageNum, int pageSize);
    List<Book> searchBook(String keyword);
    List<Book> retrieveBooksById(List<Integer> idList);
}
