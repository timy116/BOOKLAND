package com.bookland.dao;

import com.bookland.entity.CreditCard;

public interface CreditCardDAO {
    CreditCard retrieveByLast4(String last4);
    void create(CreditCard creditCard);
}
