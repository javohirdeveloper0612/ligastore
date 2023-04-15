package com.example.telegrambot.util;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class SendMsg {
    public static SendMessage sendMsg(Long id, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(id);
        sendMessage.setText(text);
        sendMessage.setParseMode("Markdown");
        return sendMessage;
    }


    public static SendMessage sendMsg(Long id, String text, ReplyKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(id);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public static SendMessage sendMsg(Long id, String text, InlineKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(id);
        sendMessage.setText(text);
        sendMessage.setParseMode("Markdown");
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public static EditMessageText sendMsgEdite(Long id, String text, InlineKeyboardMarkup markup) {
        EditMessageText sendMessage = new EditMessageText();
        sendMessage.setChatId(id);
        sendMessage.setText(text);
        sendMessage.setParseMode("Markdown");
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public static SendMessage sendMsgParse(Long chatId, String text, ReplyKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(markup);
        sendMessage.setParseMode("Markdown");
        return sendMessage;
    }


    public static SendMessage sendMsgMark(Long chatId, String text, ReplyKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        sendMessage.setParseMode("Markdown");
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public static SendPhoto sendPhoto(Long id, String text, String inputFile) {
        SendPhoto sendPhoto = new SendPhoto();
        InputFile input = new InputFile();
        input.setMedia(inputFile);
        sendPhoto.setChatId(id);
        sendPhoto.setPhoto(input);
        sendPhoto.setCaption(text);
        return sendPhoto;
    }


    public static SendDocument sendAdminDoc(Long chatId, InputFile inputFile) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(inputFile);
        return sendDocument;

    }

    public static DeleteMessage deleteMessage(Message message) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId());
        deleteMessage.setMessageId(message.getMessageId());
        return deleteMessage;

    }


    public static SendMessage sendMsgParse(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        sendMessage.setParseMode("Markdown");
        return sendMessage;
    }


    public static EditMessageText editMessageText(Long chatId, String text) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setText(text);
        editMessageText.setParseMode("Markdown");
        return editMessageText;
    }

    public static EditMessageText editMessageText(Long chatId, String text, InlineKeyboardMarkup markup) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setText(text);
        editMessageText.setParseMode("Markdown");
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

}
