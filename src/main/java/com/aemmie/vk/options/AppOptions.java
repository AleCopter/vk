package com.aemmie.vk.options;

public class AppOptions extends Options {
    public int       NEWS_WIDTH             = 470;
    public int       NEWS_HEIGHT            = 500;
    public int       SCROLL_RATE            = 20;
    public boolean   NEWS_MAX_QUALITY       = true;
    public boolean   NEWS_TEXT_FILTER       = true;
    public String    TEXT_FILTER            = "vk.cc,vk.com,goo.gl,[club,http";
    public boolean   NEWS_LIKE_FILTER       = true;
    public boolean   PRELOAD_MULTI_PHOTO    = true;

    public static AppOptions load() {
        AppOptions options = new AppOptions();
        load(AppOptions.class, options, "config.properties");
        return options;
    }

    public void save() {
        save(AppOptions.class, "config.properties");
    }
}
