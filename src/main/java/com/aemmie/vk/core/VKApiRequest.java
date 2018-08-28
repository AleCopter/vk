package com.aemmie.vk.core;

import com.aemmie.vk.auth.Auth;
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

    private static final String API_URL = "api.vk.com/method/";

    private static long lastTime = 0;

    public static Gson gson = new Gson();

    private static Logger LOGGER = LoggerFactory.getLogger(VKApiRequest.class);
    private static JsonParser parser = new JsonParser();

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
            long currentTime = System.currentTimeMillis();
            while (currentTime - lastTime < 400) {
                Thread.sleep(50);
            }
            lastTime = currentTime;

            String url = "https://" + API_URL + params.get("method");
            if (!params.containsKey("access_token")) params.put("access_token", Auth.getAccessToken());
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(url);
            request.addHeader("User-Agent", "KateMobileAndroid/48.2 lite-433 (Android 8.1.0; SDK 27; arm64-v8a; Google Pixel 2 XL; en)");
            List<NameValuePair> urlParameters = new ArrayList<>();
            for (String key : params.keySet()) {
                if (!key.equals("method")) urlParameters.add(new BasicNameValuePair(key, params.get(key)));
            }
            //if (!params.containsKey("sig")) urlParameters.add(new BasicNameValuePair("sig", getSig()));
            request.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = client.execute(request);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
            JsonObject rootObj = parser.parse(reader.readLine()).getAsJsonObject();
            return rootObj.getAsJsonObject("response");
        } catch (Exception e) {
            return null;
        }
    }

//    public String getSig() {
//        String src = "/method/" + params.get("method") + "?";
//        ArrayList<String> parts = new ArrayList<>();
//        for (String key : params.keySet()) {
//            if (!key.equals("method")) parts.add(key + '=' +params.get(key));
//        }
//        return Sig.md5(src + String.join("&", parts) + Auth.AUTH_SECRET);
//    }
}
