package com.example.util;


import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;


public class SmsSender {

    private final static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjM4MjcsInJvbGUiOm51bGwsImRhdGEiOnsiaWQiOjM4MjcsIm5hbWUiOiJPc29iZW5ubyIsImVtYWlsIjoidW1pZHN0eWxldXpAbWFpbC5ydSIsInJvbGUiOm51bGwsImFwaV90b2tlbiI6bnVsbCwic3RhdHVzIjoiYWN0aXZlIiwic21zX2FwaV9sb2dpbiI6ImVza2l6MiIsInNtc19hcGlfcGFzc3dvcmQiOiJlJCRrIXoiLCJ1el9wcmljZSI6NTAsInVjZWxsX3ByaWNlIjoxMTUsInRlc3RfdWNlbGxfcHJpY2UiOm51bGwsImJhbGFuY2UiOjQ4NTAsImlzX3ZpcCI6MCwiaG9zdCI6InNlcnZlcjEiLCJjcmVhdGVkX2F0IjoiMjAyMy0wNC0xMVQxMzowNToyNC4wMDAwMDBaIiwidXBkYXRlZF9hdCI6IjIwMjMtMDQtMTJUMTA6MjI6MTUuMDAwMDAwWiIsIndoaXRlbGlzdCI6bnVsbH0sImlhdCI6MTY4MTI5NTEwMCwiZXhwIjoxNjgzODg3MTAwfQ.GGj4-SMvy6csvI3cQOPHqbikXhmcwzUXGsrcl6n92XM";

    public static void main(String[] args) {
//        sendSms("998996731741", "salom123");
//        System.out.println(gen());

    getToken();

    }



    public static void sendSms(String phone, String message) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("mobile_phone", phone)
                    .addFormDataPart("message", message)
                    .addFormDataPart("from", "4546")
                    .addFormDataPart("callback_url", "http://0000.uz/test.php")
                    .build();
            Request request = new Request.Builder()
                    .url("https:notify.eskiz.uz/api/message/sms/send")
                    .addHeader("Authorization", "Bearer " + token)
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getToken() {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("mod", "lattocyr")
                    .addFormDataPart("text", "Sh Ch G' g' sh")
                    .addFormDataPart("ignoreHtml","true")
                    .build();
            Request request = new Request.Builder()
                    .url("https://lotin.uz/api/translate")
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();

            String jsonpObject = response.toString();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int gen() {
        Random r = new Random( System.currentTimeMillis() );
        return ((1 + r.nextInt(9)) * 10000 + r.nextInt(10000));
    }

}
