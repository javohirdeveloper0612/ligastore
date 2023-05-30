package com.example.service;



import com.example.enums.Language;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ResourceBundleService {
    private final ResourceBundleMessageSource messageSource;

    public ResourceBundleService(ResourceBundleMessageSource resourceBundleMessageSource) {
        this.messageSource = resourceBundleMessageSource;
    }

    public String getMessage(String code, String lang) {
        return messageSource.getMessage(code, null, new Locale(lang));
    }

    public String getMessage(String code, Language lang, Object... arg) {
        return messageSource.getMessage(code, arg, new Locale(lang.name()));
    }


}
