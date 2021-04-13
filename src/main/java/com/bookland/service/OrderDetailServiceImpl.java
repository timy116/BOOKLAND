package com.bookland.service;

import com.bookland.dao.OrderDetailDAO;
import com.bookland.entity.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailDAO orderDetailDAO;

    @Override
    public void create(OrderDetail orderDetail) {
        orderDetailDAO.create(orderDetail);
    }

    @Override
    public List<OrderDetail> retrieveByOrderId(Integer orderId) {
        return orderDetailDAO.retrieveByOrderId(orderId);
    }

    @Override
    public List<OrderDetail> retrieveOrderDetailsByOrderId(Integer orderId) {
        return orderDetailDAO.retrieveOrderDetailsByOrderId(orderId);
    }
}
