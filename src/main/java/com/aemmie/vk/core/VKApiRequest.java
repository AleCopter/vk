package com.aemmie.vk.core;

import com.aemmie.vk.data.Error;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class VKApiRequest {
    private static Logger LOGGER = LoggerFactory.getLogger(VKApiRequest.class);
    private static JsonParser parser = new JsonParser();

    public static Gson gson = new Gson();

    private static final String API_URL = "api.vk.com/method/";
    private static int requestsPerLastSecond = 0;

    public LinkedHashMap<String, String> params = new LinkedHashMap<>();

    public VKApiRequest(String method, boolean music) {
        params.put("method", method);
        params.put("v", "5.68");
        params.put("lang", "en");
        params.put("https", "1");
    }

    public VKApiRequest(String method) {
        params.put("method", method);
        params.put("v", "5.80");
        params.put("lang", "en");
        params.put("https", "1");
    }

    public VKApiRequest param(String name, Object value) {
        params.put(name, String.valueOf(value));
        return this;
    }

    public JsonObject run() {
        try {
            //restriction of 3 requests per second
            requestsPerLastSecond++;
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
                requestsPerLastSecond--;
            }).start();

            while (requestsPerLastSecond > 3) {
                Thread.sleep(50);
            }

            String url = "https://" + API_URL + params.get("method");
            if (!params.containsKey("access_token")) params.put("access_token", Auth.getAccessToken());
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(url);
            request.addHeader("User-Agent", "KateMobileAndroid/48.2 lite-433 (Android 8.1.0; SDK 27; arm64-v8a; Google Pixel 2 XL; en)");
            List<NameValuePair> urlParameters = new ArrayList<>();
            for (String key : params.keySet()) {
                if (!key.equals("method")) urlParameters.add(new BasicNameValuePair(key, params.get(key)));
            }
            request.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = client.execute(request);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
            JsonObject rootObj = parser.parse(reader.readLine()).getAsJsonObject();
            if (rootObj.has("error")) {
                LOGGER.error(rootObj.toString());
                Error error = gson.fromJson(rootObj.get("error"), Error.class);
                switch (error.error_code) {
                    case 5:
                        Auth.init(true);
                        break;
                }
                return null;
            }
            return rootObj.getAsJsonObject("response");
        } catch (Exception e) {
            return null;
        }
    }

}
