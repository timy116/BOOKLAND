package com.bookland.controller;

import com.bookland.entity.Book;
import com.bookland.service.BookService;
import com.bookland.service.OrderDetailService;
import com.linecorp.bot.client.LineBlobClient;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.singletonList;

@Slf4j
@LineMessageHandler
@RestController
public class LineBotController {

    @Value("${domain:}")
    String domain;

    @Autowired
    private LineMessagingClient lineMessagingClient;

    @Autowired
    private LineBlobClient lineBlobClient;

    @Autowired
    BookService bookService;

    @Autowired
    OrderDetailService orderDetailService;

    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        reply(replyToken, messages, false);
    }

    private void reply(@NonNull String replyToken,
                       @NonNull List<Message> messages,
                       boolean notificationDisabled) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages, notificationDisabled))
                    .get();
            log.info("Sent messages: {}", apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(@NonNull String replyToken, @NonNull String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }

        switch (message.trim()) {
            case "1":
                message = "我們有美術、攝影、海報書與雜誌共四種類別的書籍。";
                break;
            case "2":
                Book book = orderDetailService.retrieveBooksByOrderDetail().get(0).getBook();

                message = "我們目前賣的最好的書為這本:\n" +
                        book.getName() +
                        "\n價格為 " + book.getPrice() +
                        " 元\n" +
                        domain + "/book/insidepage/" + book.getSlug();
                break;
            case "3":
                final String[] msg = {"我們推薦您這三本書:\n\n"};
                List<Integer> idList = new ArrayList<>();

                for(int i = 0; i < 3; i ++) {
                    int id = (int) ((Math.random() * (101 - 1)) + 1);
                    if (idList.contains(id)) {
                        i --;
                        continue;
                    }
                    idList.add(id);
                }

                bookService
                        .retrieveBooksById(idList)
                        .forEach(book1 -> msg[0] += domain + "/book/insidepage/" + book1.getSlug() + "\n\n");
                message = msg[0];
                break;
            case "4":
                message = "我們目前只提供線上刷卡付款。";
                break;
            default:
                message = "您好，您有什麼問題想問的嗎?\n\n" +
                        "1 - 你們有賣哪些類別的書籍呢?\n" +
                        "2 - 你們最暢銷的書是哪一本呢?\n" +
                        "3 - 有什麼推薦的書籍嗎?\n" +
                        "4 - 你們有什麼付款方式?\n" +
                        "5 - 其他問題";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        TextMessageContent message = event.getMessage();
        this.replyText(event.getReplyToken(), message.getText());
    }
}
