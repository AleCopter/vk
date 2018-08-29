package com.aemmie.vk.app;

import com.aemmie.vk.core.Tab;
import com.aemmie.vk.news.NewsApi;

import javax.swing.*;
import java.awt.*;


public class NewsTab extends Tab {

    private JPanel panel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane(panel);

    private JPanel topPanel = new JPanel();

    private Dimension buttonsSize = new Dimension(30, 40);

    public NewsTab() {
        panel.setBackground(Color.DARK_GRAY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(null);

        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(Options.SCROLL_RATE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            JScrollBar bar = (JScrollBar) e.getSource();
            if (bar.getMaximum() > 500 && (1.0 * bar.getValue() / bar.getMaximum() > 0.9 || bar.getMaximum() - bar.getValue() < 1000) && NewsApi.ready) {
                NewsApi.updateNews(15);
            }
        });

        this.add(scrollPane);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(null);

        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        JButton refreshButton = new JButton("refresh");
        refreshButton.setFocusable(false);
        refreshButton.addActionListener(e -> {
            NewsApi.refresh();
            NewsApi.updateNews(30);
        });
        topPanel.add(refreshButton);

        NewsApi.setPanel(panel);
        //NewsApi.updateNews(30);
    }

    @Override
    public void init() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public JPanel getTopPanel() {
        return topPanel;
    }
}
