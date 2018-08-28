package com.aemmie.vk.basic;

public class Audio {
    Integer id;
    Integer owner_id;
    String artist;
    String title;
    Integer duration;
    Integer date;
    String url;
    Integer album_id;
    String access_key;

    @Override
    public String toString() {
        return artist + " - " + title;
    }

    public String getUrl() {
        return url;
    }
}
