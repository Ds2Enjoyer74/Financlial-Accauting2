package com.alexey.finuch2.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

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
            e.printStackTrace();
        }
    }
}
