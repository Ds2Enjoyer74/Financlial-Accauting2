package com.alexey.finuch2.bot;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alexey.finuch2.bot.UserText.HELP;
import static com.alexey.finuch2.bot.botuiservice.BotCreateButton.sendInlineKeyboardMessage;
import static jdk.javadoc.internal.tool.Main.execute;

public class UserStateService {
    private final Map<Long, UserState> userStates = new HashMap<>();
    private final Map<Long, List<UserClass>> userClassMap = new HashMap<>();
    private final Map<Long, UserClass> currentDraft = new HashMap<>();

    //Я не понял, какой класс должен идти после public
    public TelegramBot makeAnswer(UserText text, Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            switch (text) {
                case START ->
                    //это тоже должно уйти вместе с мапами в UserStateService
                        userStates.put(chatId, UserState.WAITING_FOR_INPUT);
                UserState state = userStates.get(chatId);
                // здесь ты вызываешь пустой конструктор которорго у тебя по сути не существует,
                // либо создай либо воткни инпут сюда
                // Понял, но как сделать - не понял
                currentDraft.put(chatId, new UserClass());
                switch (state) {
                    case WAITING_FOR_INPUT -> {
                        UserClass draft = currentDraft.get(chatId);
                        // Что с setName и message делать?

                        // здесь у тебя такого поля даже нет в UserClass и переменной message нигде нет в скоупе
                        draft.setName(message.trim());
                        userStates.put(chatId, UserState.WAITING_FOR_APPROVAL);
                    }
                    case WAITING_FOR_APPROVAL -> {
                        try {
                            // Я, пока, хз
                            // переменной message нигде нет в скоупе

                            // Тут я ваще не понял, что делать надо

                            boolean approval = String(message.trim());
                            UserClass current = currentDraft.get(chatId);

                            // это вообще полная дичь)))
                            current(approval);

                            userClassMap
                                    .computeIfAbsent(chatId, k -> new ArrayList<>())
                                    .add(current);
                            currentDraft.remove(chatId);
                            userStates.remove(chatId);
                            break;

                            case HELP ->
                            try {
                                execute(sendInlineKeyboardMessage(update.getMessage().getChatId()));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}
