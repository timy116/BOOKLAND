package com.bookland.facebook;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.bookland.entity.User;
import com.bookland.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

@Service
public class FacebookConnectionSignup implements ConnectionSignUp {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserService userService;

    @Override
    // 若該使用者是第一次從 facebook 登入，則綁訂一個新的使用者
    public String execute(Connection<?> connection) {
        final User user = new User();
        user.setUserName(connection.getDisplayName());
        user.setPassword(encoder.encode(randomAlphanumeric(32)));
        user.setEmail("!" + randomAlphanumeric(16));
        userService.create(user);
        return user.getUserName();
    }
}
