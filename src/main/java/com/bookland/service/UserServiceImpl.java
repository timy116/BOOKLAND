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
    public void updateTime(String username) {
        userDAO.updateLoginTime(username);
    }

    @Override
    public User retrieveByUserName(String username) {
        return userDAO.retrieveByUserName(username);
    }

    @Override
    public User retrieveByEmail(String email) {
        return userDAO.retrieveByEmail(email);
    }

    @Override
    public void updateUser(String userName,String name, String email, String phone, String address) {
        userDAO.updateUser(userName,name,email,phone,address);
    }

    @Override
    public void updateUserInfo(User user) {
        userDAO.updateUserInfo(user);
    }
}
