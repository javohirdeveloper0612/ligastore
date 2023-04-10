package com.example.telegrambot.myTelegrambot;

import com.example.telegrambot.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {


    private final BotConfig botConfig;


    @Autowired
    public MyTelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }


    @Override
    public void onUpdateReceived(Update update) {


    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }


}
