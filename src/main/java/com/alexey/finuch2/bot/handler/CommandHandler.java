package com.alexey.finuch2.bot.handler;

import com.alexey.finuch2.bot.service.MessageFactory;
import com.alexey.finuch2.bot.state.UserCommand;
import com.alexey.finuch2.bot.state.UserState;
import com.alexey.finuch2.bot.state.UserStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.alexey.finuch2.bot.state.UserCommand.HELP;
import static com.alexey.finuch2.bot.state.UserCommand.START;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandHandler implements TgHandler {
    private final UserStateService userStateService;

    @Override
    public void handle(MessageFactory message) {
        if (!(message instanceof MessageFactory.Command command)) {
            log.debug("Skipping {}", this.getClass().getSimpleName());
            return;
        }

        UserCommand userCommand = UserCommand.from(command);
        switch (userCommand) {
            case START -> {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(command.getChatId());
                sendMessage.setText("Hi, it's second version of FinUch bot. It's new generation!");
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            // гет можно оставить и тащить из UserStateService
            //Оно чёт не тащится. Или надо прям тут тоже Мапу эту переписывать?
            UserState state = userStates.get(chatId);
            if(state == null) {
                sendMessage( chatId, "press /start, dude");
            }
            case HELP:
                startCommandHelpAnswer(chatId);
                break;
            default -> sendMessage(chatId, "Try again, error");
        }

    }
}
