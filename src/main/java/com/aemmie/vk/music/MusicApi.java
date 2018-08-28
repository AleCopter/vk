package com.aemmie.vk.music;

import com.aemmie.vk.basic.Audio;
import com.aemmie.vk.core.VKApiRequest;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MusicApi {

    public static Logger LOGGER = LoggerFactory.getLogger(MusicApi.class);

    private static Type audioListType = new TypeToken<ArrayList<Audio>>() {}.getType();

    public static List<Audio> get(String owner_id) throws Exception {
        JsonObject json = new VKApiRequest("audio.get", true)
                .param("owner_id", owner_id).run();
        if (json != null) {
            return VKApiRequest.gson.fromJson(json.get("items"), audioListType);
        }
        return null;
    }

    public static void getPlaylists(String owner_id) throws Exception {
        String codePlaylists = "var audio = API.audio.getPlaylists({owner_id: Args.owner_id, count: 200});return {count: audio.count, items: audio.items};";
        JsonObject json = new VKApiRequest("execute", true)
                .param("owner_id", owner_id)
                .param("code", codePlaylists).run();
    }

    public static List<Audio> search(String pattern) {
        JsonObject json = new VKApiRequest("audio.search", true)
                .param("q", pattern).run();
        return null;
    }

}
