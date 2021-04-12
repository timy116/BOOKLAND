package com.bookland.dao;

import com.bookland.entity.OrderDetail;

import java.util.List;

public interface OrderDetailDAO {
    void create(OrderDetail orderDetail);
    List<OrderDetail> retrieveByOrderId(Integer orderId);
    List<OrderDetail> OrderDetail(Integer orderId);
}
