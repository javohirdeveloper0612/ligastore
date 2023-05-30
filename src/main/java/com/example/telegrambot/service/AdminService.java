package com.example.telegrambot.service;

import com.example.entity.ProfileEntity;
import com.example.entity.PromoCode;
import com.example.enums.ProfileRole;
import com.example.repository.AuthRepository;
import com.example.repository.PromoCodeRepository;
import com.example.telegrambot.constant.Constant;
import com.example.telegrambot.myTelegrambot.MyTelegramBot;
import com.example.telegrambot.util.Button;
import com.example.telegrambot.util.SendMsg;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.*;
import java.util.*;

import static com.example.entity.PromoCode.PromoCodeStatus.ACTIVE;


@Service
public class AdminService {

    private final MyTelegramBot myTelegramBot;
    private final AuthRepository authRepository;
    private final PromoCodeRepository promocodeRepository;

    @Lazy
    public AdminService(MyTelegramBot myTelegramBot, AuthRepository authRepository,
                        PromoCodeRepository promocodeRepository) {
        this.myTelegramBot = myTelegramBot;
        this.authRepository = authRepository;
        this.promocodeRepository = promocodeRepository;
    }

    /**
     * This method is used for sending main menu
     *
     * @param message Message
     */
    public void mainMenu(Message message) {



        myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                "*ASSALOMU ALEKUM ADMIN PANELGA XUSH KELIBSIZ !" +
                        " ILTIMOS O'ZINGIZGA KERAKLI MENUNI TANLANG*",
                Button.markup(Button.rowList(
                        Button.row(
                                Button.button(Constant.listOfClient),
                                Button.button(Constant.listOfPromoCode)
                        ),
                        Button.row(Button.button(Constant.searchModel))
                ))));
    }

    /**
     * This method is used for sending List Of Client converting excel
     *
     * @param message Message
     */
    public void sendListOfClient(Message message) {
        var list = authRepository.findAllByRole(ProfileRole.ROLE_USER);
        if (list.isEmpty()) {
            sendMessage(message.getChatId(), "*Mijozlar ro'yxati mavjud emas*");
            return;
        }

        var accountData = new TreeMap<Long, Object[]>();
        accountData.put(0L, new Object[]{"ID raqami ", "Ismi", "Familiyasi", "Tug'ilgan sanasi", "Kasbi",
                "Viloyat", "Tuman", "Telefon raqami", "Ball", "Holati"});

        setClientData(list, accountData);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("Mijozlar ro'yxati.xlsx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        InputFile inputFile = new InputFile();
        inputFile.setMedia(inputStream, "Mijozlar ro'yxati.xlsx");
        myTelegramBot.send(SendMsg.sendDoc(message.getChatId(), inputFile));
    }


    public void setClientData(List<ProfileEntity> list, TreeMap<Long, Object[]> accountData) {
        for (ProfileEntity profile : list) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet spreadsheet = workbook.createSheet("Mijozlar ro`yxati");
            XSSFRow row;

            accountData.put(profile.getId(), new Object[]{profile.getId().toString(), profile.getNameUz(),
                    profile.getSurnameUz(), profile.getBirthdate(), profile.getProfessionUz(),
                    profile.getRegion(), profile.getDistrict(), profile.getPhoneUser(), profile.getScore(), profile.getStatus().toString()});
            Set<Long> keyid = accountData.keySet();
            int rowid = 0;

            for (Long key : keyid) {
                row = spreadsheet.createRow(rowid++);
                Object[] objectArr = accountData.get(key);
                int cellid = 0;

                for (Object obj : objectArr) {
                    Cell cell = row.createCell(cellid++);
                    cell.setCellValue(String.valueOf(obj));
                }
            }
            writeclientData(workbook);
        }
    }

    public void writeclientData(XSSFWorkbook workbook) {
        try {
            var out = new FileOutputStream("Mijozlar ro'yxati.xlsx");
            workbook.write(out);
            out.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used for sending PromoCode list converting Excel
     *
     * @param message Message
     */
    public void sendListPromoCode(Message message) {
        var list = promocodeRepository.findAll();
        if (list.isEmpty()) {
            sendMessage(message.getChatId(), "*Promo-Code lar ro'yxati mavjud emas*");
            return;
        }
        Map<Long, Object[]> accountData = new TreeMap<>();
        accountData.put(0L, new Object[]{"ID RAQAMI ", "PROMO-CODE", "BALL", "PRODUCT-MODEL", "PRODUCT NOMI", "HOLATI"});

        for (PromoCode promoCode : list) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet spreadsheet = workbook.createSheet("Promo-Code lar ro`yxati");
            XSSFRow row;
            accountData.put(promoCode.getId(), new Object[]{promoCode.getId().toString(), promoCode.getCode(),
                    promoCode.getScore(), promoCode.getProduct().getModel(), promoCode.getProduct().getNameUz(), promoCode.getStatus()});
            Set<Long> keyid = accountData.keySet();

            int rowid = 0;
            for (Long key : keyid) {
                row = spreadsheet.createRow(rowid++);
                Object[] objectArr = accountData.get(key);
                int cellid = 0;

                for (Object obj : objectArr) {
                    Cell cell = row.createCell(cellid++);
                    cell.setCellValue(String.valueOf(obj));
                }
            }

            try {
                var out = new FileOutputStream("Promo-code lar ro'yxati.xlsx");
                workbook.write(out);
                out.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("Promo-code lar ro'yxati.xlsx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        InputFile inputFile = new InputFile();
        inputFile.setMedia(inputStream, "Promo-code lar ro'yxati.xlsx");
        myTelegramBot.send(SendMsg.sendDoc(message.getChatId(), inputFile));

    }

    /**
     * This method is used for getting Mode
     *
     * @param message Message
     */
    public void getModel(Message message) {
        sendMessage(message.getChatId(), "*Iltimos Product modelini kiriting !" +
                "\nMasalan : V1*");
    }

    /**
     * This method is used for getting PromocodeList by Model converting excel
     *
     * @param message Message
     */
    public void searchByModel(Message message) {
        var list = promocodeRepository.findAllByProductModelAndStatus(message.getText(), ACTIVE);
        if (list.isEmpty()) {
            sendMessage(message.getChatId(), message.getText() + " modeli bo'yicha promo-code lar ro'yxati topilmadi");
            return;
        }
        Map<Long, Object[]> accountData = new TreeMap<>();
        accountData.put(0L, new Object[]{"ID RAQAMI ", "PROMO-CODE", "BALL", "PRODUCT-MODEL", "PRODUCT NOMI", "HOLATI"});

        for (PromoCode promoCode : list) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet spreadsheet = workbook.createSheet("Promo-Code lar ro`yxati");
            XSSFRow row;
            accountData.put(promoCode.getId(), new Object[]{promoCode.getId().toString(), promoCode.getCode(),
                    promoCode.getScore(), promoCode.getProduct().getModel(), promoCode.getProduct().getNameUz(), promoCode.getStatus()});
            Set<Long> keyid = accountData.keySet();

            int rowid = 0;
            for (Long key : keyid) {
                row = spreadsheet.createRow(rowid++);
                Object[] objectArr = accountData.get(key);
                int cellid = 0;

                for (Object obj : objectArr) {
                    Cell cell = row.createCell(cellid++);
                    cell.setCellValue(String.valueOf(obj));
                }
            }

            try {
                var out = new FileOutputStream("Model bo'yicha Promo-code lar ro'yxati.xlsx");
                workbook.write(out);
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("Model bo'yicha Promo-code lar ro'yxati.xlsx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        InputFile inputFile = new InputFile();
        inputFile.setMedia(inputStream, "Promo-code lar ro'yxati.xlsx");
        myTelegramBot.send(SendMsg.sendDoc(message.getChatId(), inputFile));
    }


    /**
     * This method is used for sending message
     *
     * @param chatId Long
     * @param txt    String
     */
    public void sendMessage(Long chatId, String txt) {
        myTelegramBot.send(SendMsg.sendMsg(chatId,
                txt));
    }


    /**
     * This method is used for sending message
     *
     * @param message Message
     */
    public void sendError(Message message) {
        myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                "*Unknown message*"));
    }

}
