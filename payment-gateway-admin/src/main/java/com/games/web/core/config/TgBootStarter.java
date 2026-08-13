package com.games.web.core.config;

import com.games.tg.TelegramBotClient;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class TgBootStarter {

    @Autowired
    private TelegramBotClient telegramBotClient;

    private static final Logger log = LoggerFactory.getLogger(TgBootStarter.class);

    @PostConstruct
    public void init() {
        try {
            telegramBotClient.init();
        } catch (Exception e) {
            // 吞掉异常，不影响应用正常启动
        }
    }
}

