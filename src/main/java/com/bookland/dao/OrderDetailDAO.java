package com.bookland.dao;

import com.bookland.entity.Book;
import com.bookland.entity.Order;
import com.bookland.entity.OrderDetail;

import java.util.List;

public interface OrderDetailDAO {
    void create(OrderDetail orderDetail);
    List<OrderDetail> retrieveByOrderId(Integer orderId);
    List<OrderDetail> retrieveOrderDetailsByOrderId(Integer orderId);
    List<OrderDetail> retrieveBooksByOrderDetail();
}
