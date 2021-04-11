package com.bookland.service;

import com.bookland.entity.CreditCard;

public interface CreditCardService {
    CreditCard retrieveByLast4(String last4);
    void create(CreditCard creditCard);
}
