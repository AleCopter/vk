package com.aemmie.vk.app;

import com.aemmie.vk.core.Tab;
import com.aemmie.vk.music.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

import static com.aemmie.vk.app.App.exit;

public class TitleBar extends JPanel {
    private static JPanel titlebar; //this

    private static JButton playButton;
    private static JTextField musicTitle;

    private static ArrayList<Tab> tabsList = new ArrayList<>();
    private static ArrayList<JToggleButton> tabButtonsList = new ArrayList<>();

    private static int titlebar_instrument_id;

    private static final int BUTTON_SIZE = 40;
    private static final int MEDIA_BUTTON_SIZE = 25;
    private static final int MUSIC_TITLE_CHAR_LIMIT = 60;

    private static final Dimension DEFAULT_HORIZONTAL_DIM = new Dimension(20, 0);

    private static final ImageIcon PLAY_ICON = getIcon("icons/play.png", MEDIA_BUTTON_SIZE);
    private static final ImageIcon PAUSE_ICON = getIcon("icons/pause.png", MEDIA_BUTTON_SIZE);

    TitleBar() {
        titlebar = this;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setMaximumSize(new Dimension(10000, 40));
        this.setAlignmentY(Component.TOP_ALIGNMENT);
        this.setBorder(new LineBorder(Color.BLACK, 1));

        createNewTab("news.png", new NewsTab());
        //createNewTab("messages.png", null);
        //createNewTab("music.png", null);
        createNewTab("options.png", new OptionsTab());

        this.add(Box.createRigidArea(new Dimension(450, 0)));

        JButton prevButton = new JButton(getIcon("icons/prev.png", MEDIA_BUTTON_SIZE));
        prevButton.setFocusable(false);
        prevButton.setBorderPainted(false);
        prevButton.setContentAreaFilled(false);
        prevButton.addActionListener(e -> audioPrev());
        this.add(prevButton);

        playButton = new JButton(PLAY_ICON);
        playButton.setFocusable(false);
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.addActionListener(e -> audioPlayPause());
        this.add(playButton);

        JButton nextButton = new JButton(getIcon("icons/next.png", MEDIA_BUTTON_SIZE));
        nextButton.setFocusable(false);
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.addActionListener(e -> audioNext());
        this.add(nextButton);

        this.add(Box.createRigidArea(DEFAULT_HORIZONTAL_DIM));

        musicTitle = new JTextField();
        musicTitle.setFocusable(false);
        musicTitle.setBorder(null);
        musicTitle.setBackground(this.getBackground());
        musicTitle.setEditable(false);
        musicTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        this.add(musicTitle);

        this.add(Box.createRigidArea(DEFAULT_HORIZONTAL_DIM));
        titlebar_instrument_id = this.getComponentCount();
        this.add(Box.createRigidArea(DEFAULT_HORIZONTAL_DIM));
        this.add(Box.createRigidArea(DEFAULT_HORIZONTAL_DIM));

        JButton exitButton = new JButton(getIcon("icons/close2.png", BUTTON_SIZE));
        exitButton.setFocusable(false);
        exitButton.setSize(new Dimension(20, 20));
        exitButton.addActionListener(e -> exit());
        this.add(exitButton);
    }

    public static void setPlayIcon(boolean pause) {
        playButton.setIcon(pause ? PAUSE_ICON : PLAY_ICON);
    }

    public static void setMusicTitle(String title) {
        if (title.length() > MUSIC_TITLE_CHAR_LIMIT)
            musicTitle.setText(title.substring(0, MUSIC_TITLE_CHAR_LIMIT) + "...");
        else musicTitle.setText(title);
    }

    private static ImageIcon getIcon(String path, int size) {
        return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH));
    }

    private static void createNewTab(String icon, Tab tab) {
        JToggleButton button = new JToggleButton(getIcon("icons/" + icon, BUTTON_SIZE));
        button.setFocusable(false);
        button.setSelected(false);
        button.addActionListener(e -> setTab((JToggleButton) e.getSource()));
        titlebar.add(button);

        tab.init();

        tabButtonsList.add(button);
        tabsList.add(tab);
    }

    static void setTab(int tab) {
        setTab(tabButtonsList.get(tab));
    }

    private static void setTab(JToggleButton tabButton) {
        App.mainPanel.remove(1);
        Tab tabFromList = tabsList.get(tabButtonsList.indexOf(tabButton));
        App.mainPanel.add(tabFromList);
        App.mainPanel.updateUI();
        tabFromList.onUpdate();

        titlebar.remove(titlebar_instrument_id);
        titlebar.add(tabFromList.getTopPanel(), titlebar_instrument_id);
        tabButtonsList.forEach(jToggleButton -> jToggleButton.setSelected(false));
        tabButton.setSelected(true);
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
