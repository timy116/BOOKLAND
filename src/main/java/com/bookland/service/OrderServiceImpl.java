package com.bookland.service;

import com.bookland.dao.OrderDAO;
import com.bookland.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDAO orderDAO;

    @Override
    public void create(Order order) {
        orderDAO.create(order);
    }

    @Override
    public Order retrieveByLatest() {
        return orderDAO.retrieveByLatest();
    }

    @Override
    public List<Order> retrieveOrdersByUserId(Integer userId) {
        return orderDAO.retrieveOrdersByUserId(userId);
    }

    @Override
    public Order retrieveByOrderNumber(Integer orderNumber) {
        return orderDAO.retrieveByOrderNumber(orderNumber);
    }

}
