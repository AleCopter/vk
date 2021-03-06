package com.aemmie.vk.music;

import com.aemmie.vk.core.Auth;
import com.aemmie.vk.data.Audio;
import com.aemmie.vk.options.AudioOptions;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

import static com.aemmie.vk.app.titlebar.TopAudioPanel.setMusicTitle;
import static com.aemmie.vk.app.titlebar.TopAudioPanel.setPlayIcon;

public class Player {
    private static final JFXPanel fxPanel = new JFXPanel(); //init javafx
    private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

    public static AudioOptions options = AudioOptions.load();

    private static MediaPlayer mediaPlayer;

    private static Audio currentAudio;

    private static List<Audio> audioList;
    private static List<Audio> randomAudioList;

    private static boolean isChanged;

    private static JPanel panel;
    private static JSlider progressBar;

    private static Timer timer;

    public static void init() {
        updateVolume(options.AUDIO_VOLUME);

        setAudioList(Auth.getMyId());
        for (Audio audio : audioList) {
            panel.add(new AudioBox(audio));
            JSeparator separator = new JSeparator();
            separator.setMaximumSize(new Dimension(500, 10));
            panel.add(separator);
        }
        panel.updateUI();

        List<Audio> list = options.RANDOM ? randomAudioList : audioList;
        setMusicTitle(list.get(0).toString());

    }

    public static void setAudioList(String owner_id) {
        try {
            Player.audioList = MusicApi.get(owner_id);
        } catch (Exception e) {
            LOGGER.error("EXCEPTION:", e);
        }
        isChanged = true;
        if (options.RANDOM) {
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
        setPlayIcon(true);
        timerStart();
    }

    public static void pause() {
        if (mediaPlayer != null) mediaPlayer.pause();
        setPlayIcon(false);
        timerStop();
    }

    public static void play(Audio audio) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        progressBar.setValue(0);
        currentAudio = audio;
        mediaPlayer = new MediaPlayer(new Media(audio.getUrl()));
        updateVolume(options.AUDIO_VOLUME);
        mediaPlayer.setAutoPlay(true);
        setMusicTitle(currentAudio.toString());
        setPlayIcon(true);
        mediaPlayer.setOnReady(() -> {
            timerStart();
            progressBar.setMaximum((int) mediaPlayer.getMedia().getDuration().toMillis());
        });
        mediaPlayer.setOnEndOfMedia(() -> play(options.REPLAY ? currentAudio : getNext(true)));
    }

    public static void next() {
        play(getNext(true));
    }

    public static void prev() {
        play(getNext(false));
    }

    public static void toggleRandom() {
        options.RANDOM = (!options.RANDOM);
        if (options.RANDOM) {
            if (isChanged) {
                randomAudioList = new ArrayList<>(audioList);
                isChanged = false;
            }
            Collections.shuffle(randomAudioList);
        }
    }

    public static void toggleReplay() {
        options.REPLAY = !options.REPLAY;
    }

    public static Audio getNext(boolean forward) {
        List<Audio> list = options.RANDOM ? randomAudioList : audioList;
        Audio ret;
        if (currentAudio != null) {
            int index = (list.indexOf(currentAudio) + (forward ? +1 : -1)) % list.size();
            if (index < 0) index = list.size() - 1;
            ret = list.get(index);
        } else {
            ret = list.get(0);
        }
        currentAudio = ret;
        if (ret.getUrl().equals("")) {
            LOGGER.info(ret.toString() + " SKIPPED");
            return getNext(forward);
        }
        return ret;
    }

    public static Audio getCurrentAudio() {
        return currentAudio;
    }

    public static boolean isPaused() {
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED);
    }

    public static void updateVolume(int volume) {
        options.AUDIO_VOLUME = volume;
        double dvolume = 1.0 - Math.cos(volume / 100.0 * Math.PI / 2);
        if (dvolume < 0.01) dvolume = 0;
        if (mediaPlayer != null) mediaPlayer.setVolume(dvolume);
    }

    public static void setTime(int millis) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(new Duration(millis));
        }
    }

    public static void setAudioPanel(JPanel panel) {
        Player.panel = panel;
    }

    public static void setProgressBar(JSlider progressBar) {
        Player.progressBar = progressBar;
    }

    private static void timerStart() {
        if (timer == null) {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (mediaPlayer != null && progressBar != null) {
                        progressBar.setValue((int) mediaPlayer.getCurrentTime().toMillis());
                    }
                }
            }, 0, 100);
        }
    }

    private static void timerStop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
