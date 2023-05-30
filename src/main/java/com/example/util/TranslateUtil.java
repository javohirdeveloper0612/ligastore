package com.example.util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class TranslateUtil {

    public static String latinToAcrylic(String text) {

        String result = "";
        try {
            URL url = new URL("https://lotin.uz/api/translate");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");

            connection.setDoOutput(true);
            String requestBody = "{\"mod\":\"lattocyr\",\"text\":\" " + text + "\",\"ignoreHtml\":true}";
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(requestBody);
            outputStreamWriter.flush();
            outputStreamWriter.close();

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();
            JSONObject jsonObject = new JSONObject(response.toString());
            result = jsonObject.getString("result");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
