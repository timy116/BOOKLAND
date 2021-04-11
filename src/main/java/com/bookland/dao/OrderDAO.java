package com.bookland.dao;

import com.bookland.entity.Order;

public interface OrderDAO {
    void create(Order order);
    Order retrieveByLatest();
}
