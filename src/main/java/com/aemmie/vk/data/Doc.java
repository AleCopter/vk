package com.aemmie.vk.data;

import com.google.gson.JsonObject;

import java.util.List;

public class Doc {
    public Integer id;
    public Integer owner_id;
    public String title;
    public Integer size;
    public String ext;
    public String url;
    public Integer date;
    public Integer type;
    public JsonObject preview;
    public transient List<PhotoSize> sizes;
}
