package com.games.tg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class TelegramService {
    @Value("${telegram.bot.totalChatId}")
    private String totalChatId;


    private final TelegramBotClient telegramBot;

    public TelegramService(TelegramBotClient telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void pushTotalMessage(String content) {
        try {
            telegramBot.sendTextMessage(totalChatId, content);
        } catch (TelegramApiException e) {
            log.error("推送TG消息失败:", e);
        }
    }

    /**
     * 推送消息给指定 chatId
     */
    public void pushMessage(String chatId, String content) {
        try {
            telegramBot.sendTextMessage(chatId, content);
        } catch (TelegramApiException e) {
            log.error("推送TG消息失败:", e);
        }
    }
}
