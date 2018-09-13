package com.aemmie.vk.basic;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class VideoPlayer extends JPanel {

    EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
    EmbeddedMediaPlayer player = mediaPlayerComponent.getMediaPlayer();

    public VideoPlayer() {
        player.setPlaySubItems(true);
        player.prepareMedia("https://www.youtube.com/watch?v=28zOY_w3Hiw");
        player.parseMedia();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.RED);
        this.add(mediaPlayerComponent);

        JButton button = new JButton("autism");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.isPlaying()) {
                    player.pause();
                } else {
                    player.play();
                }
            }
        });
        this.add(button);

        mediaPlayerComponent.getVideoSurface().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getX() < mediaPlayerComponent.getVideoSurface().getSize().width / 2) {
                    System.out.println("tu");
                } else {
                    System.out.println("ru");
                }
            }
        });
    }

    public void play() {
        player.play();
    }

    public EmbeddedMediaPlayerComponent getComponent() {
        return mediaPlayerComponent;
    }

    public void update() {
        this.remove(0);
        this.add(mediaPlayerComponent, 0);
        this.updateUI();
    }
}
