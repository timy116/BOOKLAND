package com.bookland.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    Integer id;
    String userName;
    String password;
    String name;
    String email;
    String phone;
    String address;
    Date birthDate;
    Date lastLogin;
    Date createTime;
    List<Order> userOrders;
}
