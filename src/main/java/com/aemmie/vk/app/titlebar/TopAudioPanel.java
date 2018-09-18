package com.aemmie.vk.app.titlebar;

import com.aemmie.vk.NewClasses.AudioProgress;
import com.aemmie.vk.NewClasses.NewSliders;
import com.aemmie.vk.music.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TopAudioPanel extends JPanel {

    private static JButton playButton;
    private static JLabel musicTitle;
    private static AudioProgress ap;

    private static JSlider progressBar;

    private static final int MEDIA_BUTTON_SIZE = 22;
    private static final int MUSIC_TITLE_CHAR_LIMIT = 60;

    private static final ImageIcon PLAY_ICON = TitleBar.getIcon("icons/play.png", MEDIA_BUTTON_SIZE);
    private static final ImageIcon PAUSE_ICON = TitleBar.getIcon("icons/pause.png", MEDIA_BUTTON_SIZE);

    TopAudioPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        Color col = new Color(61,151,195);

        JToggleButton randomButton = new JToggleButton(TitleBar.getIcon("icons/replay.png", 15));
        randomButton.setMargin(new Insets(1, 1, 1, 1));
        randomButton.setFocusable(false);
        randomButton.setSelected(Player.options.REPLAY);
        randomButton.addActionListener(e -> Player.toggleReplay());
        randomButton.setBackground(col);
        leftPanel.add(randomButton);

        JToggleButton shuffleButton = new JToggleButton(TitleBar.getIcon("icons/shuffle.png", 15));
        shuffleButton.setMargin(new Insets(1, 1, 1, 1));
        shuffleButton.setFocusable(false);
        shuffleButton.setSelected(Player.options.RANDOM);
        shuffleButton.addActionListener(e -> Player.toggleRandom());
        shuffleButton.setBackground(col);
        leftPanel.add(shuffleButton);


        JPanel midTopPanel = new JPanel();
        midTopPanel.setLayout(new BoxLayout(midTopPanel, BoxLayout.X_AXIS));

        midTopPanel.setBackground(col);
        midTopPanel.setIgnoreRepaint(true);
        this.setBackground(col);

        JSlider volume = new JSlider(SwingConstants.HORIZONTAL, 8, 100, Player.options.AUDIO_VOLUME);

        volume.setBackground(col);
        volume.setUI(new NewSliders(volume));
        volume.setFocusable(false);

        volume.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            //layer.updateVolume(source.getValue());
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
                //Player.updateVolume(result);
            }
        });
        volume.setMaximumSize(new Dimension(160, 20));

        //region
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
        //endregion

        musicTitle = new JLabel(" ", SwingConstants.LEFT);
        musicTitle.setAlignmentX(LEFT_ALIGNMENT);

        musicTitle.setFocusable(false);
        musicTitle.setBorder(null);
        musicTitle.setBackground(this.getBackground());
        musicTitle.setFont(new Font("SansSerif", Font.BOLD, 20));

        progressBar = new JSlider();
        progressBar.setMinimum(0);
        progressBar.setValue(0);
        progressBar.setMaximumSize(new Dimension(400, 15));
        progressBar.setEnabled(false);
//        progressBar.setUI(new BasicSliderUI(progressBar) {
//            @Override
//            public void paintTrack(Graphics g) {
//                Graphics2D g2d = (Graphics2D) g;
//                Rectangle t = trackRect;
//                Point2D start = new Point2D.Float(t.x, t.y);
//                int width = t.width * progressBar.getValue() / progressBar.getMaximum();
//                Point2D end = new Point2D.Float(t.width, t.height);
//                Color[] colors = {Color.magenta, Color.blue, Color.cyan,
//                        Color.green, Color.yellow, Color.red};
//                LinearGradientPaint p = new LinearGradientPaint(start, end, new float[]{0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f}, colors);
//                g2d.setPaint(p);
//                g2d.fillRect(t.x, t.y, width, t.height);
//            }
//
//            @Override
//            public void paintThumb(Graphics g) {
//                super.paintThumb(g);
//            }
//        });
        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JSlider source = (JSlider)e.getSource();
                Point p = e.getPoint();
                double percent = p.x / ((double) source.getWidth());
                int range = source.getMaximum() - source.getMinimum();
                double newVal = range * percent;
                int result = (int)(source.getMinimum() + newVal);
                source.setValue(result);
                Player.setTime(result);
            }
        });
        Player.setProgressBar(progressBar);



        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        this.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        //this.setMaximumSize(new Dimension(700, 100));
        GridBagConstraints c =  new GridBagConstraints();

        c.gridx = 0; c.gridy = 0; c.gridheight = GridBagConstraints.REMAINDER;
        layout.setConstraints(leftPanel, c);
        c.gridheight = 1;
        this.add(leftPanel);

        c.gridx = 1; c.gridy = 1;
        layout.setConstraints(midTopPanel, c);
        this.add(midTopPanel);

        //c.gridx = 2; c.gridy = 1;
        //layout.setConstraints(musicTitle, c);
        //this.add(musicTitle);

        c.gridx = 1; c.gridy = 2;
        layout.setConstraints(volume, c);
        this.add(volume);

        c.gridx = 2; c.gridy = 1; c.gridheight = 2; c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 400;
        layout.setConstraints(progressBar, c);
        this.add(progressBar);


        //bound this panel to left
        //!!!!! always last
        Component glue = Box.createRigidArea(new Dimension(1, 1));
        c.gridx = 3; c.gridy = 0; c.weightx = 1;
        layout.setConstraints(glue, c);
        this.add(glue);

    }

    public static void setPlayIcon(boolean pause) {
        playButton.setIcon(pause ? PAUSE_ICON : PLAY_ICON);
    }

    public static void setMusicTitle(String title) {
       /* if (title.length() > MUSIC_TITLE_CHAR_LIMIT)
            musicTitle.setText(title.substring(0, MUSIC_TITLE_CHAR_LIMIT) + "...");
        else musicTitle.setText(title);
        */
        ap.text = title;
    }

    public static void audioPrev() { Player.prev(); }

    public static void audioNext() { Player.next(); }

    public static void audioPlayPause() {
        if (playButton.getIcon().equals(PLAY_ICON)) {
            Player.play();
        } else {
            Player.pause();
        }
    }
}
