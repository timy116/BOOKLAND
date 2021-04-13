package com.bookland.dao;

import com.bookland.entity.User;

public interface UserDAO {
    void create(User user);
    User retrieveByUserName(String username);
    User retrieveByEmail(String email);
    void updateLoginTime(String username);
    void updateUser(String userName,String name,String email,String phone,String address);
    void updateUserInfo(User user);
}
