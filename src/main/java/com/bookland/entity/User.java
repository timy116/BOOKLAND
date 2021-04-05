package com.bookland.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    Integer id;
    String userName;
    String password;
    String email;
    String phone;
    String address;
    Date birthDate;
    Date lastLogin;
    Date createTime;
}
