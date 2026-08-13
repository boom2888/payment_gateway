package com.games.tg;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Service
public class TelegramBotClient extends TelegramLongPollingBot {
    @Value("${telegram.bot.registerBot:false}")
    private Boolean registerBot;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void init() throws Exception {
        if(registerBot){
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this); // 注册Bot
            log.info("注册TG机器人:{}", botUsername);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            try {
                System.out.println("chatId=" + chatId);
                sendTextMessage(chatId, "You said: " + update.getMessage().getText() + ",chatId= " + chatId);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 使用 Spring Retry 自动重试，带指数退避
     */
    @Retryable(
            value = TelegramApiException.class,   // 哪些异常触发重试
            maxAttempts = 3,                      // 最大重试次数
            backoff = @Backoff(
                    delay = 1000,                 // 初始延迟 1 秒
                    multiplier = 2.0              // 每次乘以 2
            )
    )
    public void sendTextMessage(String chatId, String text) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        execute(message); // 如果失败会触发 Retry
    }

    @Recover
    public void recover(TelegramApiException e, String chatId, String text) {
        System.err.println("消息发送最终失败: " + text + "，原因: " + e.getMessage());
        // TODO: 可以接钉钉/企业微信告警
    }

}
