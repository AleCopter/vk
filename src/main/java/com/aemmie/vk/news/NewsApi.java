package com.aemmie.vk.news;

import com.aemmie.vk.basic.Group;
import com.aemmie.vk.core.VKApiRequest;
import com.aemmie.vk.news.classes.NewsBox;
import com.aemmie.vk.news.classes.Post;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NewsApi {
    private static Logger LOGGER = LoggerFactory.getLogger(NewsApi.class);
    private static Type postListType = new TypeToken<Collection<Post>>() {}.getType();
    private static Type groupListType = new TypeToken<Collection<Group>>() {}.getType();

    private static String next = null;

    public static boolean ready = true;
    public static Map<Integer, Group> groups = new HashMap<>();

    private static int last = 0;

    private static JPanel panel;

    public static void updateNews(int count) {
        ready = false;

        new Thread(new Runnable() {
            public void run() {
                VKApiRequest request = new VKApiRequest("newsfeed.get")
                        .param("filters", "post")
                        .param("source_ids", "list{1}")
                        .param("count", count)
                        .param("max_photos", 1);
                if (next != null) request.param("start_from", next);
                JsonObject json = request.run();
                next = json.get("next_from").getAsString();
                if (json != null) {
                    for (Group group : (Collection<Group>) VKApiRequest.gson.fromJson(json.get("groups"), groupListType)) {
                        try {
                            group.image = new ImageIcon(new ImageIcon(new URL(group.photo_50)).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                        } catch (MalformedURLException e) {
                            LOGGER.error(group.name + " PHOTO ERROR", e);
                        }
                        groups.put(group.id, group);
                    }
                    for (Post post : (Collection<Post>) VKApiRequest.gson.fromJson(json.get("items"), postListType)) {
                        int s = post.hashCode();
                        if (s == last) continue;
                        last = s;
                        post.parse();
                        panel.add(new NewsBox(post));
                        panel.updateUI();
                    }
                    ready = true;
                }
            }
        }).start();
    }

    public static void refresh() {
        next = null;
        panel.removeAll();
    }

    public static void setPanel(JPanel panel) {
        NewsApi.panel = panel;
    }
}
