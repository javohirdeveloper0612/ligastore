package com.example.telegrambot.controller;

import com.example.telegrambot.constant.Constant;
import com.example.telegrambot.constant.Step;
import com.example.telegrambot.constant.TelegramUsers;
import com.example.telegrambot.service.AdminService;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    private final AdminService adminService;

    private final List<TelegramUsers> usersList = new ArrayList<>();


    public AdminController(AdminService adminService) {

        this.adminService = adminService;
    }

    public void handler(Message message) {
        TelegramUsers telegramUsers = saveUser(message.getChatId());
        if (message.getText().equals("/start")) {
            adminService.mainMenu(message);
            telegramUsers.setStep(Step.MAIN);
            return;
        }

        if (telegramUsers.getStep() == null) {
            telegramUsers.setStep(Step.MAIN);
        }

        if (telegramUsers.getStep().equals(Step.MAIN)) {
            switch (message.getText()) {
                case Constant.listOfClient -> {
                    adminService.sendListOfClient(message);
                    return;
                }

                case Constant.listOfPromoCode -> {
                    adminService.sendListPromoCode(message);
                    return;
                }

                case Constant.searchModel -> {
                    adminService.getModel(message);
                    telegramUsers.setStep(Step.MODEL);
                    return;
                }

                default -> adminService.sendError(message);
            }
        }

        if (telegramUsers.getStep().equals(Step.MODEL)) {
            adminService.searchByModel(message);
            telegramUsers.setStep(Step.MAIN);
        }

    }


    /**
     * This method is used for saving User Data
     *
     * @param chatId Long
     * @return TelegramUsers
     */
    public TelegramUsers saveUser(Long chatId) {

        for (TelegramUsers users : usersList) {
            if (users.getChatId().equals(chatId)) {
                return users;
            }
        }
        TelegramUsers users = new TelegramUsers();
        users.setChatId(chatId);
        usersList.add(users);

        return users;
    }
}
