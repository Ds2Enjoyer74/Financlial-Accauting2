package com.alexey.finuch2.bot.service;

import com.alexey.finuch2.bot.exception.MessageNotSupportedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public sealed class MessageFactory permits MessageFactory.Text, MessageFactory.Command, MessageFactory.CallbackQuery {
    public static MessageFactory from(Update update) {
        boolean isText = update.hasMessage() && update.getMessage().hasText();
        boolean isCallbackQuery = update.hasCallbackQuery();
        boolean isCommand = update.hasMessage() && update.getMessage().isCommand();

        long chatId = Optional
                .ofNullable(update.getMessage().getChatId())
                .orElseThrow(() -> new IllegalArgumentException("chatId should not be null"));

        if (isCommand) {
            return new Command(chatId, update.getMessage().getText());
        }

        if (isCallbackQuery) {
            return new CallbackQuery(chatId, update.getCallbackQuery().getData());
        }

        if (isText) {
            return new Text(chatId, update.getMessage().getText());
        }

        throw new MessageNotSupportedException(String.format("Message with chatId: %s not supported", chatId));
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public final static class Text extends MessageFactory {
        private long chatId;
        private String text;
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public final static class Command extends MessageFactory {
        private long chatId;
        private String command;
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public final static class CallbackQuery extends MessageFactory {
        private long chatId;
        private String callbackQuery;
    }
}
