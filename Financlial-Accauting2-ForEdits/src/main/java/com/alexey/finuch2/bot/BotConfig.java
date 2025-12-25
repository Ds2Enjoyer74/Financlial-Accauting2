package com.alexey.finuch2.bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class BotConfig {
    private final TelegramBot telegramBot;

    public BotConfig(TelegramBot myTelegramBot) {
        this.telegramBot = myTelegramBot;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            //это кал который ни о чем не говорит, добавь сюда sl4j logger и пиши в log.error()
            log.error("Error in BotConfig, init");
        }
    }
}
