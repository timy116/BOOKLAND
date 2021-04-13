package com.bookland.service;

import com.bookland.entity.Order;

import java.util.List;

public interface OrderService {
    void create(Order order);
    Order retrieveByLatest();
    List<Order> retrieveOrdersByUserId(Integer userId);
    Order retrieveByOrderNumber(Integer orderNumber);
}
