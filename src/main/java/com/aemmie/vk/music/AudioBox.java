package com.aemmie.vk.music;

import com.aemmie.vk.data.Audio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AudioBox extends JPanel {

    public AudioBox(Audio audio) {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Player.play(audio);
            }
        });
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        this.setMaximumSize(new Dimension(500, 100));

        JLabel title = new JLabel(audio.getTitle());
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        this.add(title);

        JLabel artist = new JLabel(audio.getArtist());
        artist.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        this.add(artist);

    }

}
