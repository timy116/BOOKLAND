package com.bookland.facebook;

import com.bookland.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Arrays;

@Service
public class FacebookSignInAdapter implements SignInAdapter {

    @Autowired
    private UserService userService;

    @Override
    // 當使用 facebook 登入成功後
    public String signIn(String s, Connection<?> connection, NativeWebRequest nativeWebRequest) {
        String username = connection.getDisplayName();

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Arrays.asList(new SimpleGrantedAuthority("FACEBOOK_USER"))
                        )
                );
        userService.updateTime(username);
        return "/";
    }
}
