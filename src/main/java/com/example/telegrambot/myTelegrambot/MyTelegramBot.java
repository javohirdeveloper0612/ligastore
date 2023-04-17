package com.example.telegrambot.myTelegrambot;

import com.example.telegrambot.config.BotConfig;
import com.example.telegrambot.controller.AdminController;
import com.example.telegrambot.util.InlineButton;
import com.example.telegrambot.util.SendMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {


    private final BotConfig botConfig;
    private final AdminController adminController;


    @Autowired
    public MyTelegramBot(BotConfig botConfig, AdminController adminController) {
        this.botConfig = botConfig;
        this.adminController = adminController;
    }


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.getChatId().equals(1030035146L)) {
                adminController.handler(message);
                return;
            }

            if (message.getChatId().equals(1234567L)) {

            }

        } else if (update.hasCallbackQuery()) {

        }


    }


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    public void send(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(SendDocument sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(DeleteMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    public void order(Message message) {

        send(SendMsg.sendMsg(
                message.getChatId(),
                "Buyurtma qabul qilindi Buyurtma holati aktiv !",
                InlineButton.keyboardMarkup(
                        InlineButton.rowList(
                                InlineButton.row(
                                        InlineButton.button("Product topshirish", "accepted")
                                )
                        )
                )
        ));
    }

}
