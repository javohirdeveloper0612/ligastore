package com.example;


import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class Test {

    public static void main(String[] args) throws IOException {
//        getToken();

        sendSms();
    }

    public static void getToken() throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("email","umidstyleuz@mail.ru")
                .addFormDataPart("password","SpOpRopyP3IsCAciuyjMKOaDFolrCl1iXQLV1GP3")
                .build();
        Request request = new Request.Builder()
                .url("https://notify.eskiz.uz/api/auth/login")
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();

//        System.out.println(response.body().string());

        String token = new JSONObject(Objects.requireNonNull(response.body()).string())
                .getJSONObject("data")
                .getString("token");

        System.out.println(token);
    }

    public static void refresh() throws IOException {

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjM4MjcsInJvbGUiOiJ1c2VyIiwiZGF0YSI6eyJpZCI6MzgyNywibmFtZSI6Ik9PTyBPc29iZW5ubyIsImVtYWlsIjoidW1pZHN0eWxldXpAbWFpbC5ydSIsInJvbGUiOiJ1c2VyIiwiYXBpX3Rva2VuIjpudWxsLCJzdGF0dXMiOiJhY3RpdmUiLCJzbXNfYXBpX2xvZ2luIjoiZXNraXoyIiwic21zX2FwaV9wYXNzd29yZCI6ImUkJGsheiIsInV6X3ByaWNlIjo1MCwidWNlbGxfcHJpY2UiOjExNSwidGVzdF91Y2VsbF9wcmljZSI6bnVsbCwiYmFsYW5jZSI6MjkzMTIwLCJpc192aXAiOjAsImhvc3QiOiJzZXJ2ZXIxIiwiY3JlYXRlZF9hdCI6IjIwMjMtMDQtMTFUMTM6MDU6MjQuMDAwMDAwWiIsInVwZGF0ZWRfYXQiOiIyMDIzLTA1LTI5VDA0OjU1OjA0LjAwMDAwMFoiLCJ3aGl0ZWxpc3QiOm51bGwsImhhc19wZXJmZWN0dW0iOjB9LCJpYXQiOjE2ODU0MjQyNTIsImV4cCI6MTY4ODAxNjI1Mn0.lhzutGZv5ahgUgk42dOut_eETdL2W9ygPzuVqHpr-Zg";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://notify.eskiz.uz/api/auth/refresh")
                .method("PATCH", body)
                .addHeader("Authorization","Bearer "+token)
                .build();
        Response response = client.newCall(request).execute();




        System.out.println(response);


    }

    public static void sendSms() throws IOException {

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjM4MjcsInJvbGUiOiJ1c2VyIiwiZGF0YSI6eyJpZCI6MzgyNywibmFtZSI6Ik9PTyBPc29iZW5ubyIsImVtYWlsIjoidW1pZHN0eWxldXpAbWFpbC5ydSIsInJvbGUiOiJ1c2VyIiwiYXBpX3Rva2VuIjpudWxsLCJzdGF0dXMiOiJhY3RpdmUiLCJzbXNfYXBpX2xvZ2luIjoiZXNraXoyIiwic21zX2FwaV9wYXNzd29yZCI6ImUkJGsheiIsInV6X3ByaWNlIjo1MCwidWNlbGxfcHJpY2UiOjExNSwidGVzdF91Y2VsbF9wcmljZSI6bnVsbCwiYmFsYW5jZSI6MjkzMTIwLCJpc192aXAiOjAsImhvc3QiOiJzZXJ2ZXIxIiwiY3JlYXRlZF9hdCI6IjIwMjMtMDQtMTFUMTM6MDU6MjQuMDAwMDAwWiIsInVwZGF0ZWRfYXQiOiIyMDIzLTA1LTI5VDA0OjU1OjA0LjAwMDAwMFoiLCJ3aGl0ZWxpc3QiOm51bGwsImhhc19wZXJmZWN0dW0iOjB9LCJpYXQiOjE2ODU0MjU5MDMsImV4cCI6MTY4ODAxNzkwM30.2qyxjyxeOgu-YXkzn9kulM2vk7yrW34772Liibb_6wc";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("mobile_phone","998996193738")
                .addFormDataPart("message","Davay ishga go")
                .addFormDataPart("from","4546")
                .addFormDataPart("callback_url","http://0000.uz/test.php")
                .build();
        Request request = new Request.Builder()
                .url("http:notify.eskiz.uz/api/message/sms/send")
                .method("POST", body)
                .addHeader("Authorization","Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());

        System.out.println(response);
    }

}
