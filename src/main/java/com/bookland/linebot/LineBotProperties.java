package com.bookland.linebot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LineBotProperties {
    @Value("${line.bot.channel-token}")
    private String channelToken;

    @Value("${line.bot.channel-secret}")
    private String channelSecret;
}