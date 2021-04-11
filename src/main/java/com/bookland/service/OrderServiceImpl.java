package com.bookland.service;

import com.bookland.dao.OrderDAO;
import com.bookland.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDAO orderDAO;

    @Override
    public void create(Order order) {
        orderDAO.create(order);
    }
}
