package com.aemmie.vk.news.classes;

import com.aemmie.vk.basic.Audio;
import com.aemmie.vk.basic.Doc;
import com.aemmie.vk.basic.Photo;
import com.aemmie.vk.basic.PhotoSize;
import com.aemmie.vk.core.VKApiRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.util.Pair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Post {
    private static final transient Type photoType = new TypeToken<Photo>() {}.getType();
    private static final transient Type audioType = new TypeToken<Audio>() {}.getType();
    private static final transient Type docType = new TypeToken<Doc>() {}.getType();
    private static final transient Type photoSizeType = new TypeToken<ArrayList<PhotoSize>>() {}.getType();

    public String type;
    public Integer source_id;
    Integer date;
    String post_type;
    String text;
    public Integer marked_as_ads;
    JsonArray attachments;
    JsonObject photos;
    public Likes likes;
    public Views views;


    public transient List<Pair<String, Object>> attachmentsList;

    public Post() {
        attachmentsList = new LinkedList<>();
    }

    public void parse() {
        if (attachments!=null) {
            for (JsonElement attachment : attachments) {
                JsonObject attachmentRoot = attachment.getAsJsonObject();
                String type = attachmentRoot.get("type").getAsString();
                switch (type) {
                    case "photo":
                        attachmentsList.add(new Pair<>("photo", VKApiRequest.gson.fromJson(attachmentRoot.getAsJsonObject("photo"), photoType)));
                        break;
                    case "audio":
                        attachmentsList.add(new Pair<>("audio", VKApiRequest.gson.fromJson(attachmentRoot.getAsJsonObject("audio"), audioType)));
                        break;
                    case "doc":
                        Pair<String, Object> pair = new Pair<>("doc", VKApiRequest.gson.fromJson(attachmentRoot.getAsJsonObject("doc"), docType));
                        Doc doc = (Doc)pair.getValue();
                        if (doc.preview.has("photo")) doc.sizes = VKApiRequest.gson.fromJson(doc.preview.get("photo").getAsJsonObject().get("sizes"), photoSizeType);
                        attachmentsList.add(pair);
                        break;
                    default:
                        attachmentsList.add(new Pair<>(type, null));
                }
            }
        } else if (photos!=null) {
            attachmentsList.add(new Pair<>("photo", VKApiRequest.gson.fromJson(photos.getAsJsonArray("items").get(0), photoType)));
        }
        attachments=null;
        photos=null;
    }

    @Override
    public int hashCode() {
        return Math.abs(source_id+date);
    }
}
class Likes {
    public Integer count;
}
class Views {
    public Integer count;
}