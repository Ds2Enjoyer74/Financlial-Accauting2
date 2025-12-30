package com.alexey.finuch2.bot.handler;

import com.alexey.finuch2.bot.service.MessageFactory;

import java.util.function.Consumer;

public interface TgHandler {
    void handle(MessageFactory message, Consumer<>);
}
