package com.alexey.finuch2.bot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TelegramBot extends TelegramLongPollingBot {

    // эти три мапки утащи в отдельный класс UserStateService и логика мутки со стейтом должна быть в нем
    // это Single responsibility из SOLID
    private final Map<Long, UserState> userStates = new HashMap<>();
    private final Map<Long, List<UserClass>> userClassMap = new HashMap<>();
    private final Map<Long, UserClass> currentDraft = new HashMap<>();


    @Override
    public void onUpdateReceived(Update update) {
        // здесь нужны классы обертки чтобы ты из апдейта вот с помощью этих ифов мог создавать нужные объекты оберток,
        // юзай Factory pattern
        if (update.hasMessage() && update.getMessage().hasText()) {
           String messageText = update.getMessage().getText();
           long chatId = update.getMessage().getChatId();

           switch (messageText) {
               case "/start":
                   //это тоже должно уйти вместе с мапами в UserStateService
                   userStates.put(chatId, UserState.WAITING_FOR_INPUT);
                   // здесь ты вызываешь пустой конструктор которорго у тебя по сути не существует,
                   // либо создай либо воткни инпут сюда
                   //и это тоже должно уйти вместе с мапами в UserStateService
                   currentDraft.put(chatId, new UserClass());
                   //вот это тут можно оставить, ибо это уже бота часть
                   startCommandRecieved(chatId);
                   break;

                   // гет можно оставить и тащить из UserStateService
                   UserState state = userStates.get(chatId);
                   if(state == null) {
                       sendMessage( chatId, "press /start, dude");
                   }
                   switch (state) {
                       case WAITING_FOR_INPUT -> {
                           UserClass draft = currentDraft.get(chatId);
                           // здесь у тебя такого поля даже нет в UserClass и переменной message нигде нет в скоупе
                           draft.setName(message.trim());
                           userStates.put(chatId, UserState.WAITING_FOR_APPROVAL);

                           //это оставляй, а остальное выше тоже часть логики UserStateService
                           sendMessage(chatId, "Are you sure?");
                       }
                       case WAITING_FOR_APPROVAL -> {
                           try {
                               //Я, пока, хз
                               //переменной message нигде нет в скоупе
                               boolean approval = String (message.trim());
                               UserClass current = currentDraft.get(chatId);

                               //это вообще полная дичь)))
                               current(approval);

                               userClassMap
                                       .computeIfAbsent(chatId, k -> new ArrayList<>())
                                       .add(current);
                               currentDraft.remove(chatId);
                               userStates.remove(chatId);

                               //это оставляй, а остальное выше тоже часть логики UserStateService
                               sendMessage(chatId, "You approved this!");
                           }
                       }
                   }

               case "/help":
                   try {
                       execute(sendInlineKeyboardMessage(update.getMessage().getChatId()));
                   } catch (TelegramApiException e) {
                       throw new RuntimeException(e);
                   }
                   break;
               default:
                   sendMessage(chatId, "Try again, error");
           }
        }
        else if (update.hasCallbackQuery()) {
            try {
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                message.setText(update.getCallbackQuery().getData());
                execute(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void startCommandRecieved(Long chatId) {
        String answer = "Hi, it's second version of FinUch bot. It's new generation!";
        sendMessage(chatId, answer);

    }

    private void sendMessage(Long chatId, String messagetoSend ) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(messagetoSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    //это утащи вообще в отдельный пакет с именем ui и в отдельный класс для создания кнопок и прочей шляпы,
    // назови хз типо BotUiService или что-то типо этого
    public static SendMessage sendInlineKeyboardMessage(long chatId) {
        //Inline keyboard here
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        // If need another button, just douplicate this code
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("It's a button");
        inlineKeyboardButton.setCallbackData("Button has been pressed");

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(inlineKeyboardButton);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Just for example");
        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }



    //это не секурно, я прям щас могу твоего бота тогда спиздить утащи это в конфиги и не пуш никогда
    @Override
    public String getBotUsername() {
        return "@AgregatorPriceForYou_bot";
    }

    //это не секурно, я прям щас могу твоего бота тогда спиздить утащи это в конфиги и не пуш никогда
    @Override
    public String getBotToken() {
        return "8437355068:AAHnzXW8GhL_zzT12LQTM1-ubEB-JYKDKY0";
    }
}
