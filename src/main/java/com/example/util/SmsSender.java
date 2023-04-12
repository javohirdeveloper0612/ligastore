package com.example.util;

import okhttp3.*;

import java.io.IOException;

public class SmsSender {

    public static void sendSms(String phone,String message){
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("mobile_phone",phone)
                    .addFormDataPart("message",message)
                    .addFormDataPart("from","4546")
                    .addFormDataPart("callback_url","http://0000.uz/test.php")
                    .build();
            Request request = new Request.Builder()
                    .url("notify.eskiz.uz/api/message/sms/send")
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();

        }   catch (IOException e) {
        throw new RuntimeException(e);
    }
    }


}
