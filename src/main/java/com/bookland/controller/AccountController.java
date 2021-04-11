package com.bookland.controller;

import com.bookland.entity.Book;
import com.bookland.entity.CreditCard;
import com.bookland.entity.Order;
import com.bookland.entity.OrderDetail;
import com.bookland.service.*;
import com.bookland.utils.SnowFlakeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.bookland.utils.CookUtil.getCartItems;
import static com.bookland.utils.CookUtil.getCookie;

@Controller
@Slf4j
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/success")
    public String paymentSuccess(HttpServletRequest request, HttpServletResponse response, String sessionId)
            throws JsonProcessingException {
        if (sessionId == null) return "redirect:/";
        Session session;
        PaymentIntent intent;
        Charge charge;
        Charge.PaymentMethodDetails.Card card;

        try {
            session = Session.retrieve(sessionId);
            intent = PaymentIntent.retrieve(session.getPaymentIntent());
            charge = intent.getCharges().getData().get(0);
            card = charge.getPaymentMethodDetails().getCard();
        } catch (StripeException e) {
            log.debug("Error: {}", e.getMessage());
            return "redirect:/";
        }

        int total;
        AtomicInteger quantity = new AtomicInteger();
        Cookie cookie = getCookie(request, "cart");
        Map<String, Integer> cart = getCartItems(cookie);
        List<Integer> idList = new ArrayList<>();
        cart.keySet().forEach(s -> idList.add(Integer.parseInt(s)));
        List<Book> books = bookService.retrieveBooksById(idList);
        total = books.stream().mapToInt(book -> book.getPrice() * cart.get(Integer.toString(book.getId()))).sum();
        cart.forEach((s, integer) -> quantity.addAndGet(integer));

        // 取得當前使用者
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String last4 = card.getLast4();
        CreditCard creditCard = creditCardService.retrieveByLast4(last4);

        // 如果信用卡不存在則新增信用卡
        if (ObjectUtils.isEmpty(creditCard)) {
            CreditCard obj = new CreditCard();
            obj.setLast4(Integer.parseInt(card.getLast4()));
            obj.setCardExpMonth(card.getExpMonth().toString());
            obj.setCardExpYear(card.getExpYear().toString());
            obj.setBrand(card.getBrand());
            creditCardService.create(obj);
            creditCard = creditCardService.retrieveByLast4(last4);
        }

        // 新增訂單至資料庫
        Order order = new Order();
        order.setOrderNumber(new SnowFlakeUtil(1, 1).getNextId());
        order.setPrice(total);
        order.setQuantity(quantity.get());
        order.setUserId(userService.retrieveByUserName(user.getUsername()).getId());
        order.setCreditCardId(creditCard.getId());
        orderService.create(order);

        // 取得剛剛新增的訂單資料
        order = orderService.retrieveByLatest();

        // 新增訂單詳情至資料庫
        Order finalOrder = order;
        books.forEach(book -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(finalOrder.getId());
            orderDetail.setBookId(book.getId());
            orderDetail.setQuantity(cart.get(book.getId().toString()));
            orderDetailService.create(orderDetail);
        });

        // 完成後清除 Cookie
        cookie = new Cookie("cart", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:account";
    }

    @GetMapping("/account")
    public String accountInformation(String username){
        return "account";
    }

    @PostMapping("/accountUpdate")
    public String accountUpdete(String userName,String name,String email,String phone,String address){
        userService.updateUser(userName,name,email,phone,address);
        return "account";
    }
}
