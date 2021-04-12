package com.bookland.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetail {
    Integer id;
    Integer orderId;
    Integer bookId;
    Integer quantity;
    Order order;
    List<Book> bookDetails;
    CreditCard creditCard;
}
