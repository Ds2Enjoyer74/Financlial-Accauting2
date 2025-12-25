package com.alexey.finuch2.bot.botuiservice;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class BotCreateButton {

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
}
