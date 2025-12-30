package com.alexey.finuch2.bot.handler;

import com.alexey.finuch2.bot.service.MessageFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextMessageHandler implements TgHandler {
    @Override
    public void handle(MessageFactory message) {
        if (!(message instanceof MessageFactory.Text textMessage)) {
            log.debug("Skipping {}", this.getClass().getSimpleName());
            return;
        }

        log.info("got message with chatId: {} with text: {}", textMessage.getChatId(), textMessage.getText());
    }
}
