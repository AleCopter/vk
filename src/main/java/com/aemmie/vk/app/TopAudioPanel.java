package com.aemmie.vk.app;

import com.aemmie.vk.music.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TopAudioPanel extends JPanel {

    private static JButton playButton;
    private static JLabel musicTitle;

    private static final int MEDIA_BUTTON_SIZE = 22;
    private static final int MUSIC_TITLE_CHAR_LIMIT = 60;

    private static final Dimension DEFAULT_HORIZONTAL_DIM = new Dimension(20, 0);

    private static final ImageIcon PLAY_ICON = TitleBar.getIcon("icons/play.png", MEDIA_BUTTON_SIZE);
    private static final ImageIcon PAUSE_ICON = TitleBar.getIcon("icons/pause.png", MEDIA_BUTTON_SIZE);

    TopAudioPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        this.add(leftPanel);

        JToggleButton randomButton = new JToggleButton(TitleBar.getIcon("icons/replay.png", 15));
        randomButton.setMargin(new Insets(1, 1, 1, 1));
        randomButton.setFocusable(false);
        randomButton.setSelected(Player.options.REPLAY);
        randomButton.addActionListener(e -> Player.toggleReplay());
        leftPanel.add(randomButton);

        JToggleButton shuffleButton = new JToggleButton(TitleBar.getIcon("icons/shuffle.png", 15));
        shuffleButton.setMargin(new Insets(1, 1, 1, 1));
        shuffleButton.setFocusable(false);
        shuffleButton.setSelected(Player.options.RANDOM);
        shuffleButton.addActionListener(e -> Player.toggleRandom());
        leftPanel.add(shuffleButton);


        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.Y_AXIS));
        this.add(midPanel);

        JPanel midTopPanel = new JPanel();
        midTopPanel.setLayout(new BoxLayout(midTopPanel, BoxLayout.X_AXIS));
        midPanel.add(midTopPanel);

        JSlider volume = new JSlider(SwingConstants.HORIZONTAL, 8, 100, Player.options.AUDIO_VOLUME);
        volume.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            Player.updateVolume(source.getValue());
        });
        volume.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JSlider source = (JSlider)e.getSource();
                Point p = e.getPoint();
                double percent = p.x / ((double) source.getWidth());
                int range = source.getMaximum() - source.getMinimum();
                double newVal = range * percent;
                int result = (int)(source.getMinimum() + newVal);
                source.setValue(result);
                Player.updateVolume(result);
            }
        });
        volume.setMaximumSize(new Dimension(160, 20));
        midPanel.add(volume);

        JButton prevButton = new JButton(TitleBar.getIcon("icons/prev.png", MEDIA_BUTTON_SIZE));
        prevButton.setFocusable(false);
        prevButton.setBorderPainted(false);
        prevButton.setContentAreaFilled(false);
        prevButton.addActionListener(e -> audioPrev());
        midTopPanel.add(prevButton);

        playButton = new JButton(PLAY_ICON);
        playButton.setFocusable(false);
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.addActionListener(e -> audioPlayPause());
        midTopPanel.add(playButton);

        JButton nextButton = new JButton(TitleBar.getIcon("icons/next.png", MEDIA_BUTTON_SIZE));
        nextButton.setFocusable(false);
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.addActionListener(e -> audioNext());
        midTopPanel.add(nextButton);

        this.add(Box.createRigidArea(DEFAULT_HORIZONTAL_DIM));

        musicTitle = new JLabel();
        musicTitle.setFocusable(false);
        musicTitle.setBorder(null);
        musicTitle.setBackground(this.getBackground());
        musicTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        this.add(musicTitle);
    }

    public static void setPlayIcon(boolean pause) {
        playButton.setIcon(pause ? PAUSE_ICON : PLAY_ICON);
    }

    public static void setMusicTitle(String title) {
        if (title.length() > MUSIC_TITLE_CHAR_LIMIT)
            musicTitle.setText(title.substring(0, MUSIC_TITLE_CHAR_LIMIT) + "...");
        else musicTitle.setText(title);
    }

    static void audioPrev() { Player.prev(); }

    static void audioNext() { Player.next(); }

    static void audioPlayPause() {
        if (playButton.getIcon().equals(PLAY_ICON)) {
            Player.play();
        } else {
            Player.pause();
        }
    }
}
