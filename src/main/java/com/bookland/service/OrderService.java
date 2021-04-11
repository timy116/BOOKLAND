package com.bookland.service;

import com.bookland.entity.Order;

public interface OrderService {
    void create(Order order);
    Order retrieveByLatest();
}
