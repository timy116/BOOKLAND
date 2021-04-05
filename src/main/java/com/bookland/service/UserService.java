package com.bookland.service;

import com.bookland.entity.User;

public interface UserService {
    User retrieveByUserName(String username);
    User retrieveByEmail(String email);
}
