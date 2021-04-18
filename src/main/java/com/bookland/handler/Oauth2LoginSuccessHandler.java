package com.bookland.handler;

import com.bookland.Google.GoogleOauth2User;
import com.bookland.entity.User;
import com.bookland.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Component
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        GoogleOauth2User googleOauth2User = new GoogleOauth2User(authentication.getPrincipal());
        String email = googleOauth2User.getEmail();
        if(ObjectUtils.isEmpty(userService.retrieveByEmail(googleOauth2User.getEmail())))
        {
            final User user = new User();
            user.setUserName(googleOauth2User.getEmail());
            user.setPassword(encoder.encode(randomAlphanumeric(32)));
            user.setEmail(googleOauth2User.getEmail());
            userService.create(user);
        }
        userService.updateTime(email);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
