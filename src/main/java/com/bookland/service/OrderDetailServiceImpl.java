package com.bookland.service;

import com.bookland.dao.OrderDetailDAO;
import com.bookland.entity.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailDAO orderDetailDAO;

    @Override
    public void create(OrderDetail orderDetail) {
        orderDetailDAO.create(orderDetail);
    }
}
