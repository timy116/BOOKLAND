package com.bookland.controller;

import com.bookland.entity.Book;
import com.bookland.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.bookland.utils.CookUtil.getCartItems;
import static org.springframework.web.util.WebUtils.getCookie;

@Controller
@Slf4j
public class CheckoutController {

    @Autowired
    BookService bookService;

    @Value("${stripe.secretKey}")
    String secretKey;

    @Value("${domain}")
    String domain;

    @Value("${AWS_S3}")
    String S3;

    @PostMapping("checkout")
    @ResponseBody
    public String checkout(HttpServletRequest request, @RequestBody Map<String, Integer> p)
            throws StripeException, JsonProcessingException {
        HashMap<String, String> responseData = new HashMap<>();

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            responseData.put("id", null);
            return new ObjectMapper().writeValueAsString(responseData);
        }

        // 設定私鑰
        Stripe.apiKey = secretKey;

        // 取得 Cookie
        Cookie cookie = getCookie(request, "cart");
        List<Object> paymentMethodTypes = new ArrayList<>();

        // 設定付款方式為刷卡
        paymentMethodTypes.add("card");
        List<Object> lineItems = new ArrayList<>();

        if (!ObjectUtils.isEmpty(cookie)) {
            List<Integer> idList = new ArrayList<>();
            Map<String, Integer> cart = getCartItems(cookie);

            cart.keySet().forEach(s -> idList.add(Integer.parseInt(s)));
            List<Book> books = bookService.retrieveBooksById(idList);

            books.forEach(book -> {
                try {
                    // 設定書籍圖片，可在刷卡頁面看到
                    List<String> imgUrls = new ArrayList<>();
                    imgUrls.add(S3 + book.getImageUrl());

                    Map<String, Object> lineItem = new HashMap<>();

                    // 透過 Stripe API 創建商品，目的是讓前端修改無效
                    Map<String, Object> productParams = new HashMap<>();
                    productParams.put("name", book.getName());
                    productParams.put("images", imgUrls);
                    Product product = Product.create(productParams);

                    // 透過 Stripe API 創建價格，並指定商品
                    Map<String, Object> priceParams = new HashMap<>();

                    // * 100 後才是正確的金額
                    priceParams.put("unit_amount", book.getPrice() * 100);
                    priceParams.put("currency", "twd");
                    priceParams.put("product", product.getId());
                    Price price = Price.create(priceParams);

                    lineItem.put("price", price.getId());
                    lineItem.put("quantity", cart.get(book.getId().toString()));
                    lineItems.add(lineItem);
                } catch (StripeException e) {
                    e.printStackTrace();
                }
            });

            // 運費
            if (p.get("s") == 1) {
                Map<String, Object> lineItem = new HashMap<>();

                Map<String, Object> productParams = new HashMap<>();
                productParams.put("name", "運費");
                Product product = Product.create(productParams);

                Map<String, Object> priceParams = new HashMap<>();
                priceParams.put("unit_amount", 80 * 100);
                priceParams.put("currency", "twd");
                priceParams.put("product", product.getId());
                Price price = Price.create(priceParams);

                lineItem.put("price", price.getId());
                lineItem.put("quantity", 1);
                lineItems.add(lineItem);
            }

            Map<String, Object> params = new HashMap<>();
            params.put("success_url", domain + "/account/success?sessionId={CHECKOUT_SESSION_ID}");
            params.put("cancel_url", domain + "/cart");
            params.put("payment_method_types", paymentMethodTypes);
            params.put("line_items", lineItems);
            params.put("mode", "payment");

            Session session = Session.create(params);
            responseData.put("id", session.getId());
        } else {
            responseData.put("id", null);
        }
        return new ObjectMapper().writeValueAsString(responseData);
    }
}
