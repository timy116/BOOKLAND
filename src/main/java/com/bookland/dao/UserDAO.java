package com.bookland.dao;

import com.bookland.entity.User;

public interface UserDAO {
    void create(User user);
    User retrieveByUserName(String username);
    User retrieveByEmail(String email);
}
