package com.aemmie.vk.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class Options {
    public int       NEWS_WIDTH             = 470;
    public int       NEWS_HEIGHT            = 500;
    public int       SCROLL_RATE            = 20;
    public boolean   NEWS_MAX_QUALITY       = true;
    public boolean   NEWS_TEXT_FILTER       = true;
    public String    TEXT_FILTER            = "vk.cc,vk.com,goo.gl,[club";
    public boolean   NEWS_LIKE_FILTER       = true;
    public boolean   PRELOAD_MULTI_PHOTO    = true;
    public int       AUDIO_VOLUME           = 15;

    public void save() {
        try(OutputStream os = new FileOutputStream("config.properties")) {
            Properties props = new Properties();

            for (Field field : Options.class.getDeclaredFields()) {
                props.setProperty(field.getName(), field.get(this).toString());
            }

            props.store(os, null);
        } catch (Exception ignored) {}
    }

    public static Options load() {
        Options options = new Options();
        try {
            Properties props = new Properties();
            try (FileInputStream propStream = new FileInputStream("config.properties")) {
                props.load(propStream);
            }
            for (Field field : Options.class.getDeclaredFields()) {
                Object value = getValue(props, field.getName(), field.getType());
                if (value != null) field.set(options, value);
            }
        } catch (Exception ignored) { }
        return options;
    }

    private static Object getValue(Properties props, String name, Class<?> type) {
        String value = props.getProperty(name);
        if (value == null) return null;
        if (type == boolean.class)
            return Boolean.parseBoolean(value);
        if (type == int.class)
            return Integer.parseInt(value);
        if (type == float.class)
            return Float.parseFloat(value);
        return value;
    }
}
