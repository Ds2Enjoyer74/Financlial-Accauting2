package com.alexey.finuch2.bot.state;

import com.alexey.finuch2.bot.service.MessageFactory;

public enum UserCommand {
    START,
    HELP;

    public static UserCommand from(MessageFactory.Command command) {
        return switch (command.getCommand()) {
            case "/start" -> START;
            case "/help" -> HELP;
            case null, default -> throw new IllegalArgumentException("Command not supported");
        };
    }
}
