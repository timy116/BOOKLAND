package com.bookland.dao;

import com.bookland.entity.Book;

import java.util.List;

public interface BookDAO {
    Book retrieveBySlug(String slug);
    List<Book> retrieveAll();
    List<Book> retrieveBooks(String category);
    List<Book> listAllBooksByPage();
    List<Book> listBooksByPage(String category);
    List<Book> searchBook(String keyword);
    List<Book> retrieveBooksById(List<Integer> idList);
    List<Book> retrieveNewBooks();
}
