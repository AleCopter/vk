package com.aemmie.vk.app.tabs;

import com.aemmie.vk.basic.SmoothMouseWheel;
import com.aemmie.vk.core.Global;
import com.aemmie.vk.music.Player;

import javax.swing.*;
import java.awt.*;

public class AudioTab extends Tab {
    public JPanel panel = new JPanel();
    private JScrollPane scrollPane;// = new JScrollPane(panel);

    private JPanel topPanel = new JPanel();

    private Dimension buttonsSize = new Dimension(30, 40);

    public AudioTab() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(null);
        panel.setOpaque(false);

        scrollPane = new JScrollPane(panel) {
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(Global.background, 0, 0, this);
            }
        };

        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(0);
        scrollPane.addMouseWheelListener(new SmoothMouseWheel(scrollPane));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setOpaque(false);

        this.add(scrollPane);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(null);

        Player.setAudioPanel(panel);
    }


    @Override
    public void init() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public JPanel getTopPanel() {
        return null;
    }
}
