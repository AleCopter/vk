package com.aemmie.vk.core;

import com.aemmie.vk.auth.Auth;

public class Request {
    private String base;
    private StringBuilder sb;
    public Request(String method, int access) {
        base = "https://api.vk.com/method/";
        sb = new StringBuilder(method);
        if (access > 0) sb.append("?access_token=").append(Auth.getAccessToken());
    }

    public Request(String base) {
        this.base = base;
        sb = new StringBuilder();
    }

    public Request appendMethod(String key) {
        sb.append("/method/").append(key);
        return this;
    }

    public Request append(String key, String value) {
        this.sb.append('&').append(key).append('=').append(value);
        return this;
    }

    public Request version() {
        sb.append("&v=5.80");
        return this;
    }

//    public Request sig() {
//        try {
//            String sig = Sig.convert(MessageDigest.getInstance("MD5").digest((sb.toString() + Auth.API_SECRET).getBytes())).toString();
//            this.append("sig", sig);
//        } catch (Exception e) { }
//        return this;
//    }

    public String toString() {
        return base + sb.toString();
    }
}
