package com.aemmie.vk.music;

import com.aemmie.vk.app.App;
import com.aemmie.vk.basic.Audio;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private static final JFXPanel fxPanel = new JFXPanel(); //init javafx
    private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

    private static MediaPlayer mediaPlayer;

    private static Audio currentAudio;

    private static List<Audio> audioList;
    private static List<Audio> randomAudioList;
    private static double volume = 0.1;

    private static boolean mediaRandom;
    private static boolean isChanged;

    public static void setAudioList(String owner_id) {
        try {
            Player.audioList = MusicApi.get(owner_id);
        } catch (Exception e) {
            LOGGER.error("EXCEPTION:", e);
        }
        isChanged = true;
    }

    public static void playFirst() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Audio audio = getNext();
        mediaPlayer = new MediaPlayer(new Media(audio.getUrl()));
        configure();
    }

    public static void play(Audio audio) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer(new Media(audio.getUrl()));
        configure();
    }

    public static void skip() {
        Audio audio = getNext();
        mediaPlayer.stop();
        mediaPlayer.dispose(); //TODO: MEMORY LEAK!!!!
        mediaPlayer = null;
        mediaPlayer = new MediaPlayer(new Media(audio.getUrl()));
        configure();
    }

    public static void toggleRandom() {
        mediaRandom = (!mediaRandom);
        if (mediaRandom) {
            if (isChanged) {
                randomAudioList = new ArrayList<>(audioList);
                isChanged = false;
            }
            Collections.shuffle(randomAudioList);
        }
    }

    public static Audio getNext() {
        List<Audio> list = mediaRandom ? randomAudioList : audioList;
        Audio ret;
        if (currentAudio != null) {
            ret = list.get((list.indexOf(currentAudio)+1) % list.size());
        } else {
            ret = list.get(0);
        }
        currentAudio = ret;
        App.setMusicTitle(currentAudio.toString());
        App.setPlayIcon(true);
        if (ret.getUrl().equals("")) {
            LOGGER.info(ret.toString() + " SKIPPED");
            return getNext();
        }
        LOGGER.info(ret.toString());
        return ret;
    }

    private static void configure() {
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override public void run() {
                mediaPlayer.stop();
                Audio audio = getNext();
                mediaPlayer = new MediaPlayer(new Media(audio.getUrl()));
                configure();
            }
        });
    }

    public static Audio getCurrentAudio() {
        return currentAudio;
    }
}
