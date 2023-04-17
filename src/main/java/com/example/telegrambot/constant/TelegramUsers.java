package com.example.telegrambot.constant;

import com.example.telegrambot.constant.Step;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUsers {

    private Long chatId;

    private Step step;

}
