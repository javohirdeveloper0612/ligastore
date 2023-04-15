package com.example.telegrambot.myTelegrambot;

import com.example.service.ProductService;
import com.example.telegrambot.config.BotConfig;
import com.example.telegrambot.util.InlineButton;
import com.example.telegrambot.util.SendMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {


    private final BotConfig botConfig;


    @Autowired
    public MyTelegramBot(BotConfig botConfig) {

        this.botConfig = botConfig;
    }


    @Override
    public void onUpdateReceived(Update update) {
        String data = update.getCallbackQuery().getData();
        Message message = update.getCallbackQuery().getMessage();

        if (update.hasCallbackQuery()) {
            if (data.equals("accept")) {
                order(message);
            } else if (data.equals("accepted")) {
                send(SendMsg.deleteMessage(message));
                send(SendMsg.sendMsg(message.getChatId(), "*Buyurtmani Mijoz olib ketdi !*"));
            } else {
                send(SendMsg.sendMsg(message.getChatId(), "*Buyurtma rad etildi !*"));
            }
        } else {
            send(SendMsg.sendMsg(message.getChatId(), "*Unknown command*"));
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
