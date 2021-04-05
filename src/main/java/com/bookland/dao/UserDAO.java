package com.bookland.dao;

import com.bookland.entity.User;

public interface UserDAO {
    User retrieveByUserName(String username);
    User retrieveByEmail(String email);
}
