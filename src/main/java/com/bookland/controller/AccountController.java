package com.bookland.controller;

import com.bookland.entity.Book;
import com.bookland.entity.CreditCard;
import com.bookland.entity.Order;
import com.bookland.entity.OrderDetail;
import com.bookland.service.*;
import com.bookland.utils.SnowFlakeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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

    @GetMapping("/account/success")
    public String paymentSuccess(HttpServletRequest request, HttpServletResponse response, String sessionId, Model model)
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

        if (!ObjectUtils.isEmpty(request.getSession().getAttribute("s")))
            total += 80;


        // 取得當前使用者
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        com.bookland.entity.User currentUser = userService.retrieveByUserName(user.getUsername());
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
        order.setUserId(currentUser.getId());
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
        return "redirect:/account?success=true";
    }

    @GetMapping("/account")
    public String accountInformation(String username, Model model, String success){
        if (!ObjectUtils.isEmpty(success)) model.addAttribute("isSuccess", true);
        User userDetail = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        com.bookland.entity.User user = userService.retrieveByUserName(userDetail.getUsername());
        model.addAttribute("orders", orderService.retrieveOrdersByUserId(user.getId()));
        return "account";
    }

    @PostMapping("/accountUpdate")
    public String accountUpdete(String userName,String name,String email,String phone,String address){
        userService.updateUser(userName,name,email,phone,address);
        return "account";
    }


    @GetMapping("/account/order-list")
    @ResponseBody
    public String orderList(String orderNumber) throws JsonProcessingException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Order order = orderService.retrieveByOrderNumber(Integer.parseInt(orderNumber));
        com.bookland.entity.User user = order.getUser();
        CreditCard card = order.getCreditCard();

        List<OrderDetail> orderDetails = orderDetailService.retrieveOrderDetailsByOrderId(order.getId());
        List<Object> details = new ArrayList<>();

        Map<String, Object> data = new HashMap<>();

        Map<String, Object> orderObj = new HashMap<>();
        Map<String, Object> cardObj = new HashMap<>();
        Map<String, Object> userObj = new HashMap<>();

        orderObj.put("orderNumber", order.getOrderNumber());
        orderObj.put("price", order.getPrice());
        orderObj.put("createTime", sdf.format(order.getCreateTime()));

        cardObj.put("last4", card.getLast4());
        cardObj.put("brand", card.getBrand());

        userObj.put("userName", user.getUserName());
        userObj.put("name", user.getName());
        userObj.put("phone", user.getPhone());
        userObj.put("address", user.getAddress());

        orderObj.put("creditCard", cardObj);
        orderObj.put("user", userObj);

        orderDetails.forEach(orderDetail -> {
            Map<String, Object> obj = new HashMap<>();
            Book book = orderDetail.getBook();
            obj.put("name", book.getName());
            obj.put("slug", book.getSlug());
            obj.put("bookNumber", book.getBookNumber());
            obj.put("price", book.getPrice());
            obj.put("quantity", orderDetail.getQuantity());
            details.add(obj);
        });

        data.put("order", orderObj);
        data.put("orderDetails", details);

        return new ObjectMapper().writeValueAsString(data);
    }
}
