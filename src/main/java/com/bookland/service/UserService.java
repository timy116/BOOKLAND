package com.bookland.service;

import com.bookland.entity.User;

public interface UserService {
    void create(User user);
    void updateTime(String username);
    User retrieveByUserName(String username);
    User retrieveByEmail(String email);
    void updateUser(String userName,String name,String email,String phone,String address);
    void updateUserInfo(User user);
}
