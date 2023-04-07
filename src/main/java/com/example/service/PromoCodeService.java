package com.example.service;

import com.example.dto.ResponsePromCode;
import com.example.enums.Language;
import com.example.exception.NotMatchException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PromoCodeService {
    private final ResourceBundleService resourceBundleService;

    public PromoCodeService(ResourceBundleService resourceBundleService) {
        this.resourceBundleService = resourceBundleService;
    }

    public ResponsePromCode generateCode(double money, int amount, Language language) {


        Random random = new Random();

        if (amount == 0 || amount < 0) {
            throw new NotMatchException(resourceBundleService.getMessage("", language));
        }
        int code = random.nextInt(111111, 999999);
        return null;
    }


}
