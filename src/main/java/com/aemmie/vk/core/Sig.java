package com.aemmie.vk.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class Sig {

    private static final char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static Logger LOGGER = LoggerFactory.getLogger(Sig.class);

    public static String md5(String h) {
        try {
            return convert(MessageDigest.getInstance("MD5").digest(h.getBytes("UTF-8"))).toString();
        } catch (Exception e) {
            LOGGER.error("MD5 EXCEPTION:", e);
            return "";
        }
    }

    public static CharSequence convert(byte[] b) {
        StringBuilder ret = new StringBuilder();
        for (byte aB : b) {
            ret.append(hex[(aB & 0xF0) >> 4]);
            ret.append(hex[aB & 15]);
        }
        return ret;
    }
}
