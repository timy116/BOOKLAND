package com.bookland.service;

import com.bookland.dao.UserDAO;
import com.bookland.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public void create(User user) {
        userDAO.create(user);
    }

    @Override
    public User retrieveByUserName(String username) {
        return userDAO.retrieveByUserName(username);
    }

    @Override
    public User retrieveByEmail(String email) {
        return userDAO.retrieveByEmail(email);
    }
}
