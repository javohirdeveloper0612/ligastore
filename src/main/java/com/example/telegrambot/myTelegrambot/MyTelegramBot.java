package com.example.telegrambot.myTelegrambot;

import com.example.telegrambot.config.BotConfig;
import com.example.telegrambot.controller.AdminController;
import com.example.telegrambot.util.SendMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
        Message message = update.getMessage();
        if (update.hasMessage()) {

            if (message.getChatId().equals(1615192209L)) {
                adminController.handler(message);
            }

        } else {
            send(SendMsg.sendMsg(message.getChatId(), "Unknown message"));
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



}
