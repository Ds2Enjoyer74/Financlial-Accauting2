package com.alexey.finuch2.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
           String messageText = update.getMessage().getText();
           long chatId = update.getMessage().getChatId();

           switch (messageText) {
               case "/start":
                   startCommandRecieved(chatId);
                   break;
               case "/help":
                   try {
                       execute(sendInlineKeyboardMessage(update.getMessage().getChatId()));
                   } catch (TelegramApiException e) {
                       throw new RuntimeException(e);
                   }
                   break;
               default:
                   sendMessage(chatId, "Try again, error");
           } if (update.hasCallbackQuery()) {
               try {
                   execute(new SendMessage().setText(
                           update.getCallbackQuery().getData()
                                   .setChatId(update.getCallbackQuery().getMessage().getChatId())));
               } catch (Exception e) {
                   throw new RuntimeException(e);
               }
            }
        }

    }

    private void startCommandRecieved(Long chatId) {
        String answer = "Hi,it's second version of FinUch bot. It's new generation!";
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
        return new SendMessage().setChatId(chatId).setText("Just for example").setReplyMarkup(inlineKeyboardMarkup);
    }



    @Override
    public String getBotUsername() {
        return "";
    }
}
