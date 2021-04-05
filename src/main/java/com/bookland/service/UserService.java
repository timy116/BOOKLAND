package com.bookland.service;

import com.bookland.entity.User;

public interface UserService {
    void create(User user);
    User retrieveByUserName(String username);
    User retrieveByEmail(String email);
}
