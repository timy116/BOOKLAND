package com.bookland.service;

import com.bookland.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    void create(OrderDetail orderDetail);
    List<OrderDetail> retrieveByOrderId(Integer orderId);
    List<OrderDetail> OrderDetail(Integer orderNumber);
}
