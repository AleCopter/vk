package com.aemmie.vk.app.tabs;

import com.aemmie.vk.basic.SmoothMouseWheel;
import com.aemmie.vk.core.Global;
import com.aemmie.vk.news.NewsApi;

import javax.swing.*;
import java.awt.*;


public class NewsTab extends Tab {

    private JPanel panel = new JPanel();
    private JScrollPane scrollPane;

    private JPanel topPanel = new JPanel();

    private Dimension buttonsSize = new Dimension(30, 40);

    public NewsTab() {
        panel.setBackground(Color.DARK_GRAY);
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
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            JScrollBar bar = (JScrollBar) e.getSource();
            if (NewsApi.ready && (bar.getMaximum() - bar.getValue() < 5000)) {
                NewsApi.updateNews(5);
            }
        });
        scrollPane.getViewport().setOpaque(false);

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
        NewsApi.updateNews(30);
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
