package com.bookland.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreditCard {
    Integer id;
    Integer last4;
    String cardExpMonth;
    String cardExpYear;
    String brand;
}
