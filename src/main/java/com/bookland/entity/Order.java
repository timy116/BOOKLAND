package com.bookland.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    Integer id;
    Integer orderNumber;
    Integer price;
    Integer quantity;
    Integer transactionStatus;
    Integer userId;
    Integer creditCardId;
    Date createTime;
}
