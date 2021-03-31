package com.bookland.service;

import com.bookland.dao.BookDAO;
import com.bookland.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDAO bookDAO;

    @Override
    public List<Book> all() {
        return bookDAO.all();
    }
}
