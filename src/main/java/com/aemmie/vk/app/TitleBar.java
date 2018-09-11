package com.aemmie.vk.app;

import com.aemmie.vk.core.Tab;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

import static com.aemmie.vk.app.App.exit;

public class TitleBar extends JPanel {
    private static JPanel titlebar; //this

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
        createNewTab("messages.png", new MessagesTab());
        createNewTab("music.png", new AudioTab());
        createNewTab("options.png", new OptionsTab());

        this.add(Box.createRigidArea(new Dimension(370, 0)));

        this.add(new TopAudioPanel());

        this.add(Box.createHorizontalGlue());
        this.add(Box.createRigidArea(DEFAULT_HORIZONTAL_DIM));
        titlebar_instrument_id = this.getComponentCount();
        this.add(Box.createRigidArea(DEFAULT_HORIZONTAL_DIM));
        this.add(Box.createRigidArea(DEFAULT_HORIZONTAL_DIM));

        JButton exitButton = new JButton(getIcon("icons/close2.png", BUTTON_SIZE));
        exitButton.setFocusable(false);
        exitButton.setAlignmentX(RIGHT_ALIGNMENT);
        exitButton.setSize(new Dimension(20, 20));
        exitButton.addActionListener(e -> exit());
        this.add(exitButton);
    }

    static ImageIcon getIcon(String path, int size) {
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


        JComponent top = tabFromList.getTopPanel();
        titlebar.remove(titlebar_instrument_id);
        if (top != null) {
            titlebar.add(top, titlebar_instrument_id);
        } else {
            titlebar.add(Box.createRigidArea(DEFAULT_HORIZONTAL_DIM), titlebar_instrument_id);
        }

        tabButtonsList.forEach(jToggleButton -> jToggleButton.setSelected(false));
        tabButton.setSelected(true);
    }


}
