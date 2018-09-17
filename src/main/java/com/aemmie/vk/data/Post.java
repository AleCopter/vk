package com.aemmie.vk.data;

import com.aemmie.vk.core.VKApiRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.vk.api.sdk.objects.video.Video;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private static final transient Type photoType = new TypeToken<Photo>() {
    }.getType();
    private static final transient Type audioType = new TypeToken<Audio>() {
    }.getType();
    private static final transient Type docType = new TypeToken<Doc>() {
    }.getType();
    private static final transient Type photoSizeType = new TypeToken<ArrayList<PhotoSize>>() {
    }.getType();

    public String type;
    public Integer source_id;
    public Integer date;
    public String post_type;
    public String text;
    public Integer marked_as_ads;
    public JsonArray attachments;
    public Likes likes;
    public Views views;

    public transient List<Photo> photoList;
    public transient List<Audio> audioList;
    public transient List<Video> videoList; //TODO: add video class
    public transient List<Doc> docList;   //TODO: add other doc types (now only GIF)

    public void parse() {
        if (attachments != null) {
            for (JsonElement attachment : attachments) {
                JsonObject attachmentRoot = attachment.getAsJsonObject();
                String type = attachmentRoot.get("type").getAsString();
                switch (type) {
                    case "photo":
                        if (photoList == null) photoList = new ArrayList<>();
                        photoList.add(VKApiRequest.gson.fromJson(attachmentRoot.getAsJsonObject("photo"), photoType));
                        break;
                    case "audio":
                        if (audioList == null) audioList = new ArrayList<>();
                        audioList.add(VKApiRequest.gson.fromJson(attachmentRoot.getAsJsonObject("audio"), audioType));
                        break;
                    case "doc":
                        Doc doc = VKApiRequest.gson.fromJson(attachmentRoot.getAsJsonObject("doc"), docType);
                        if (doc.preview.has("photo")) {
                            doc.sizes = VKApiRequest.gson.fromJson(doc.preview.get("photo").getAsJsonObject().get("sizes"), photoSizeType);
                            if (docList == null) docList = new ArrayList<>();
                            docList.add(doc);
                        }
                        break;
                }
            }
        }
        attachments = null;
    }

    @Override
    public int hashCode() {
        return Math.abs(source_id + date);
    }
}

