package com.aemmie.vk.music;

import com.aemmie.vk.app.App;
import com.aemmie.vk.app.TitleBar;
import com.aemmie.vk.auth.Auth;
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
    private static boolean mediaReplay;
    private static boolean isChanged;

    public static void init() {
        updateVolume(App.options.AUDIO_VOLUME);
        setAudioList(Auth.getMyId());
    }

    public static void setAudioList(String owner_id) {
        try {
            Player.audioList = MusicApi.get(owner_id);
        } catch (Exception e) {
            LOGGER.error("EXCEPTION:", e);
        }
        isChanged = true;
        if (mediaRandom) {
            toggleRandom();
            toggleRandom(); //reload random list
        }
    }

    public static void playFirst() {
        play(getNext(true));
    }

    public static void play() {
        if (mediaPlayer == null) playFirst();
        if (isPaused()) mediaPlayer.play();
        TitleBar.setPlayIcon(true);
    }

    public static void pause() {
        if (mediaPlayer != null) mediaPlayer.pause();
        TitleBar.setPlayIcon(false);
    }

    public static void play(Audio audio) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        mediaPlayer = new MediaPlayer(new Media(audio.getUrl()));
        mediaPlayer.setVolume(volume);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(() -> play(mediaReplay ? currentAudio : getNext(true)));
    }

    public static void next() {
        play(getNext(true));
    }

    public static void prev() {
        play(getNext(false));
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

    public static void toggleReplay() {
        mediaReplay = !mediaReplay;
    }

    public static Audio getNext(boolean forward) {
        List<Audio> list = mediaRandom ? randomAudioList : audioList;
        Audio ret;
        if (currentAudio != null) {
            int index = (list.indexOf(currentAudio) + (forward ? +1 : -1)) % list.size();
            if (index < 0) index = list.size() - 1;
            ret = list.get(index);
        } else {
            ret = list.get(0);
        }
        currentAudio = ret;
        TitleBar.setMusicTitle(currentAudio.toString());
        TitleBar.setPlayIcon(true);
        if (ret.getUrl().equals("")) {
            LOGGER.info(ret.toString() + " SKIPPED");
            return getNext(forward);
        }
        LOGGER.info(ret.toString());
        return ret;
    }

    public static Audio getCurrentAudio() {
        return currentAudio;
    }

    public static boolean isPaused() {
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED);
    }

    public static void updateVolume(int volume) {
        double dvolume = 1.0 - Math.cos(volume / 100.0 * Math.PI / 2);
        if (dvolume < 0.01) dvolume = 0;
        LOGGER.info(String.valueOf(dvolume));
        Player.volume = dvolume;
        if (mediaPlayer != null) mediaPlayer.setVolume(dvolume);
    }
}
