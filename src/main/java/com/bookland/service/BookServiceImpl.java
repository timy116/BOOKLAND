package com.bookland.service;

import com.bookland.dao.BookDAO;
import com.bookland.entity.Book;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDAO bookDAO;

    @Override
    public Book retrieveBySlug(String slug) {
        return bookDAO.retrieveBySlug(slug);
    }

    @Override
    public List<Book> retrieveAll() {
        return bookDAO.retrieveAll();
    }

    @Override
    public List<Book> retrieveBooks(String category) {
        return bookDAO.retrieveBooks(category);
    }

    @Override
    public List<Book> listAllBooksByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return bookDAO.listAllBooksByPage();
    }

    @Override
    public List<Book> listBooksByPage(String category, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return bookDAO.listBooksByPage(category);
    }

    @Override
    public List<Book> searchBook(String keyword) {
        return bookDAO.searchBook(keyword);
    }

    @Override
    public List<Book> retrieveBooksById(List<Integer> idList) {
        return bookDAO.retrieveBooksById(idList);
    }
}
