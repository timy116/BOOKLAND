package com.bookland.utils;

import com.bookland.Google.GoogleOauth2User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class UserUtil {

    public String getUserName(Object user){
        user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username=null;
        if (!user.equals("anonymousUser")) {
            try {
                GoogleOauth2User googleOauth2User = new GoogleOauth2User(user);
                username = googleOauth2User.getEmail();
            } catch (ClassCastException e) {
                if(user instanceof String){
                    username = (String) user;
                }
                else
                    username = ((User) user).getUsername();
            }
        }
        return username;
    }
}
