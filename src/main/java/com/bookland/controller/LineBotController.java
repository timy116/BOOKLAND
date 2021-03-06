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
                message = "????????????????????????????????????????????????????????????????????????";
                break;
            case "2":
                Book book = orderDetailService.retrieveBooksByOrderDetail().get(0).getBook();

                message = "???????????????????????????????????????:\n" +
                        book.getName() +
                        "\n????????? " + book.getPrice() +
                        " ???\n" +
                        domain + "/book/insidepage/" + book.getSlug();
                break;
            case "3":
                final String[] msg = {"???????????????????????????:\n\n"};
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
                message = "??????????????????????????????????????????";
                break;
            default:
                message = "????????????????????????????????????????\n\n" +
                        "1 - ?????????????????????????????????????\n" +
                        "2 - ?????????????????????????????????????\n" +
                        "3 - ????????????????????????????\n" +
                        "4 - ????????????????????????????\n" +
                        "5 - ????????????";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        TextMessageContent message = event.getMessage();
        this.replyText(event.getReplyToken(), message.getText());
    }
}
