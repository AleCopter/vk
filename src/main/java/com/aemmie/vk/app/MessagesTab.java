package com.aemmie.vk.app;

import com.aemmie.vk.core.Tab;

import javax.swing.*;
import java.awt.*;

public class MessagesTab extends Tab {
    public JPanel panel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane(panel);

    MessagesTab() {
        panel.setBackground(Color.DARK_GRAY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(null);

        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(App.options.SCROLL_RATE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> { });

        this.add(scrollPane);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(null);

        panel.add(new TODO());
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
