package com.aemmie.vk.basic;

import java.util.List;

public class PhotoSize {
    Character type;
    public String url;
    public String src;
    Integer width;
    Integer height;

    public static PhotoSize get(List<PhotoSize> sizeList, Character type) {
        for (PhotoSize photo : sizeList) {
            if (photo.type.equals(type)) return photo;
        }
        return sizeList.get(0);
    }

    public static PhotoSize getMaxQuality(List<PhotoSize> sizeList) {
        Integer size = 0;
        PhotoSize maxPhoto = sizeList.get(0);
        for (PhotoSize photo : sizeList) {
            Integer thisSize = photo.width + photo.height;
            if (thisSize > size) {
                size = thisSize;
                maxPhoto = photo;
            }
        }
        return maxPhoto;
    }
}
