package com.bookland.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CookieUtil {
    static public Cookie getCookie(HttpServletRequest request, String cookieName) {
        Optional<Cookie> opt = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findAny();
        return opt.orElse(null);
    }

    static public Map<String, Integer> getCartItems(Cookie cookie) throws JsonProcessingException {
        return new ObjectMapper()
                .readValue(cookie.getValue(), new TypeReference<HashMap<String, Integer>>() {
                });
    }
}
