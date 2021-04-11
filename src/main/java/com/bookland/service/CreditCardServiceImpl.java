package com.bookland.service;

import com.bookland.dao.CreditCardDAO;
import com.bookland.entity.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    @Autowired
    private CreditCardDAO creditCardDAO;

    @Override
    public CreditCard retrieveByLast4(String last4) {
        return creditCardDAO.retrieveByLast4(last4);
    }

    @Override
    public void create(CreditCard creditCard) {
        creditCardDAO.create(creditCard);
    }
}
