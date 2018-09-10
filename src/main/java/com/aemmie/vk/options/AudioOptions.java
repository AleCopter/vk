package com.aemmie.vk.options;

public class AudioOptions extends Options {

    public int       AUDIO_VOLUME           = 15;
    public boolean   REPLAY                 = false;
    public boolean   RANDOM                 = false;

    public static AudioOptions load() {
        AudioOptions options = new AudioOptions();
        load(AudioOptions.class, options, "audio.properties");
        return options;
    }

    public void save() {
        save(AudioOptions.class, "audio.properties");
    }
}
