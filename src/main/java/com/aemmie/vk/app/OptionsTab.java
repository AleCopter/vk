package com.aemmie.vk.app;

import com.aemmie.vk.core.Tab;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.awt.*;

public class OptionsTab extends Tab {

    private JPanel panel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane(panel);

    private JPanel topPanel = new JPanel();

    private EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private Dimension buttonsSize = new Dimension(100, 40);

    public OptionsTab() {
        panel.setBackground(Color.DARK_GRAY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(null);

        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(Options.SCROLL_RATE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.add(scrollPane);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(null);

        //topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setLayout(new GridLayout(1, 0));
        JButton loadDefault = new JButton("default");
        loadDefault.setFocusable(false);
        //loadDefault.setMaximumSize(buttonsSize);
        topPanel.add(loadDefault);
        JButton cancel = new JButton("cancel");
        cancel.setFocusable(false);
        //cancel.setMaximumSize(buttonsSize);
        topPanel.add(cancel);
        JButton save = new JButton("save");
        save.setFocusable(false);
        //save.setMaximumSize(buttonsSize);
        topPanel.add(save);



        //panel.add(new WebBrowser().setURL(url).add());

        Canvas videoSurface = new Canvas();
        videoSurface.setBackground(Color.black);

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        EmbeddedMediaPlayer player = mediaPlayerComponent.getMediaPlayer();
        player.setVideoSurface(new MediaPlayerFactory().newVideoSurface(videoSurface));
        player.setPlaySubItems(true);
        String url2 = "https://www.youtube.com/watch?v=28zOY_w3Hiw";
        player.prepareMedia(url2);
        panel.add(videoSurface);
    }

    @Override
    public void init() {

    }

    public void play() {
        String url = "https://cs634103.vkuservideo.net/u8691463/videos/80def9c5fe.720.mp4?extra=83ylt4znpG3nT2Gj2EQeS77QjsWKPfQHOUNH9yLaDBhuSNBb7E4BzNTgREiyrAwUVV0z0HtM8IVoWZGP5vocND8zQ--gn6_vniMlnq8qJKx2bnKjAQzODTUskwoxV4QhYZJvSUpkIg";

        //App.frame.setContentPane(mediaPlayerComponent);
        //mediaPlayerComponent.getMediaPlayer().playMedia(url2);
        mediaPlayerComponent.getMediaPlayer().play();
    }

    @Override
    public void onUpdate() {
        //panel.updateUI();
        //testPlayer.updateUI();
    }

    @Override
    public JPanel getTopPanel() {
        return topPanel;
    }
}
