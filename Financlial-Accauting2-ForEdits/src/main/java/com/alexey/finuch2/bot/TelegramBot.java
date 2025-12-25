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

import static com.alexey.finuch2.bot.UserText.HELP;
import static com.alexey.finuch2.bot.UserText.START;
import static com.alexey.finuch2.bot.botuiservice.BotCreateButton.sendInlineKeyboardMessage;

@Component
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TelegramBot extends TelegramLongPollingBot {

    // эти три мапки утащи в отдельный класс UserStateService и логика мутки со стейтом должна быть в нем
    // это Single responsibility из SOLID



    @Override
    public void onUpdateReceived(Update update) {
        // здесь нужны классы обертки чтобы ты из апдейта вот с помощью этих ифов мог создавать нужные объекты оберток,
        // юзай Factory pattern
        if (update.hasMessage() && update.getMessage().hasText()) {
           String messageText = update.getMessage().getText();
           long chatId = update.getMessage().getChatId();

           switch (messageText) {
               case START:
                   //вот это тут можно оставить, ибо это уже бота часть
                   startCommandRecieved(chatId);
                   break;

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

    private void startCommandHelpAnswer(Long chatId) {
        String answer = "It`s help answer";
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



    //На то что ниже, пока пофиг, потом утащу


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
