package com.aemmie.vk.options;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Properties;

class Options {

    void save(Class cl, String file) {
        try(OutputStream os = new FileOutputStream(file)) {
            Properties props = new Properties();

            for (Field field : cl.getDeclaredFields()) {
                if (!field.getName().equals("file"))
                props.setProperty(field.getName(), field.get(this).toString());
            }

            props.store(os, null);
        } catch (Exception ignored) {}
    }

    static void load(Class cl, Options options, String file) {
        try {
            Properties props = new Properties();
            try (FileInputStream propStream = new FileInputStream(file)) {
                props.load(propStream);
            }
            for (Field field : cl.getDeclaredFields()) {
                if (!field.getName().equals("file")){
                    Object value = getValue(props, field.getName(), field.getType());
                    if (value != null) field.set(options, value);
                }
            }
        } catch (Exception ignored) { }
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
