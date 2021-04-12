package com.bookland.dao;

import com.bookland.entity.Order;

import java.util.List;

public interface OrderDAO {
    void create(Order order);
    Order retrieveByLatest();
    List<Order> retrieveOrdersByUserId(Integer userId);
}
