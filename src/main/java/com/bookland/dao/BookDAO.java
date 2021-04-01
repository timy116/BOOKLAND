package com.bookland.dao;

import com.bookland.entity.Book;

import java.util.List;

public interface BookDAO {
    List<Book> retrieveAll();
    List<Book> retrieveBooks(String category);
    List<Book> listAllBooksByPage();
    List<Book> listBooksByPage(String category);
}
