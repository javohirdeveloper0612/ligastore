package com.example.service;

import com.example.dto.auth.SendSmsDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.enums.ProfileStatus;
import com.example.repository.AuthRepository;
import com.example.util.MD5;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
public class SmsService {

    private final AuthRepository repository;

    @Autowired
    public SmsService(AuthRepository repository) {
        this.repository = repository;
    }

   /* public static void main(String[] args) {
        sendSmsCode("998900085120","Misolni yech mazgi ");
    }
*/
    public static void sendSmsCode(String phone, String message) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("mobile_phone", phone)
                    .addFormDataPart("message", "LigaStore:\nTasdiqlash kodi: " + message)
                    .addFormDataPart("from", "4546")
                    .addFormDataPart("callback_url", "http://0000.uz/test.php")
                    .build();
            Request request = new Request.Builder().url("https:notify.eskiz.uz/api/message/sms/send")
                    .addHeader("Authorization", "Bearer " + getToken())
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String randomSmsCode() {
        Random r = new Random(System.currentTimeMillis());
        return String.valueOf(((1 + r.nextInt(9)) * 10000 + r.nextInt(10000)));
    }

    public static String removePlusSign(String phoneNumber) {
        if (phoneNumber.startsWith("+")) {
            return phoneNumber.substring(1);
        }
        return phoneNumber;
    }


    @Transactional
    public String sendSms(ProfileEntity entity, SendSmsDTO dto, ProfileStatus status, Language language) {
        String smsCode = randomSmsCode();
        entity.setSmsCode(MD5.md5(smsCode));
        entity.setSmsTime(LocalDateTime.now());
        entity.setPhoneUser(dto.getPhone());
        entity.setStatus(status);

        repository.save(entity);

        sendSmsCode(removePlusSign(dto.getPhone()), smsCode);

        return "Tasdiqlash sms habar yuborildi";
    }

    public static String getToken() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("email", "umidstyleuz@mail.ru")
                .addFormDataPart("password", "SpOpRopyP3IsCAciuyjMKOaDFolrCl1iXQLV1GP3")
                .build();
        Request request = new Request.Builder()
                .url("https://notify.eskiz.uz/api/auth/login")
                .method("POST", body)
                .build();
        Response response = null;

        try {
            response = client.newCall(request).execute();
            return new JSONObject(Objects.requireNonNull(response.body()).string())
                    .getJSONObject("data")
                    .getString("token");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
