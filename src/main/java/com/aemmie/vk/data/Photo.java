package com.aemmie.vk.data;

import java.util.List;

public class Photo {
    public Integer id;
    public Integer album_id;
    public Integer owner_id;
    public Integer user_id;
    public String text;
    public Integer date;
    public List<PhotoSize> sizes;
}
