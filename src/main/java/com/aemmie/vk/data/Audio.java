package com.aemmie.vk.data;

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

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
