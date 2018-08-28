package com.aemmie.vk.app;

import com.aemmie.vk.auth.Auth;
import com.aemmie.vk.music.Player;
import com.aemmie.vk.news.NewsApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import static java.lang.System.exit;

public class App {
    private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static JFrame frame;
    public static JPanel listPane = new JPanel();

    private static JPanel titlebar = new JPanel();
    private static JPanel mainPanel = new JPanel();

    private static JScrollPane newsPanel = new JScrollPane(listPane);
    private static JPanel messagePanel = new JPanel();
    private static JPanel musicPanel = new JPanel();

    private static JToggleButton newsButton;
    private static JToggleButton messagesButton;
    private static JToggleButton musicButton;

    private static JButton playButton;

    private static JTextField musicTitle;

    private static final int BUTTON_SIZE = 40;
    private static final int MEDIA_BUTTON_SIZE = 25;
    private static final int MUSIC_TITLE_CHAR_LIMIT = 60;

    private static final Dimension DEFAULT_HORIZONTAL_DIM = new Dimension(20, 0);

    private static final ImageIcon PLAY_ICON = getIcon("icons/play.png", MEDIA_BUTTON_SIZE);
    private static final ImageIcon PAUSE_ICON = getIcon("icons/pause.png", MEDIA_BUTTON_SIZE);

    private static void initialize() {
        frameInit();
        listPane.setBackground(Color.DARK_GRAY);
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.Y_AXIS));

        newsPanel.getVerticalScrollBar().setUnitIncrement(32);
        newsPanel.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                JScrollBar bar = (JScrollBar) e.getSource();
                if (bar.getMaximum() > 500 && 1.0 * bar.getValue()/bar.getMaximum() > 0.9 && NewsApi.ready) {
                    NewsApi.updateNews(15);
                }
            }
        });
        newsPanel.setBorder(null);
        listPane.setBorder(null);

        NewsApi.updateNews(70);
        //NewsApi.updateNews(20);

        //Player.setAudioList(Auth.getMyId());
        //Player.toggleRandom();
        //Player.playFirst();
        //Player.test();
//        button1.setText(Player.getCurrentAudio().toString());
//        button1.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Player.skip();
//                button1.setText(Player.getCurrentAudio().toString());
//            }
//        });
    }

    private static void frameInit() {
        frame = new JFrame();
        frame.add(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        titlebarInit();
        mainPanel.add(titlebar);
        mainPanel.add(newsPanel);


        frame.setIconImage( new ImageIcon("icons/vk2.png").getImage());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("VK client");
        frame.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.height -= Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration()).bottom;

        screenSize.width++;
        frame.setSize(screenSize);
    }

    private static void titlebarInit() {
        titlebar.setLayout(new BoxLayout(titlebar, BoxLayout.X_AXIS));
        titlebar.setMaximumSize(new Dimension(10000, 40));
        titlebar.setAlignmentY(Component.TOP_ALIGNMENT);
        titlebar.setBorder(new LineBorder(Color.BLACK, 1));

        newsButton = new JToggleButton(getIcon("icons/news.png", BUTTON_SIZE));
        newsButton.setFocusable(false);
        newsButton.setSelected(true);
        newsButton.addActionListener(e -> {
            newsButton.setSelected(true);
            messagesButton.setSelected(false);
            musicButton.setSelected(false);
            mainPanel.remove(1);
            mainPanel.add(newsPanel);
            mainPanel.updateUI();
        });
        titlebar.add(newsButton);

        messagesButton = new JToggleButton(getIcon("icons/messages.png", BUTTON_SIZE));
        messagesButton.setFocusable(false);
        messagesButton.addActionListener(e -> {
            newsButton.setSelected(false);
            messagesButton.setSelected(true);
            musicButton.setSelected(false);
            mainPanel.remove(1);
            mainPanel.add(messagePanel);
            mainPanel.updateUI();
        });
        titlebar.add(messagesButton);

        musicButton = new JToggleButton(getIcon("icons/music.png", BUTTON_SIZE));
        musicButton.setFocusable(false);
        musicButton.addActionListener(e -> {
            newsButton.setSelected(false);
            messagesButton.setSelected(false);
            musicButton.setSelected(true);
            mainPanel.remove(1);
            mainPanel.add(musicPanel);
            mainPanel.updateUI();
        });
        titlebar.add(musicButton);

        titlebar.add(Box.createRigidArea(new Dimension(450, 0)));

        JButton prevButton = new JButton(getIcon("icons/prev.png", MEDIA_BUTTON_SIZE));
        prevButton.setFocusable(false);
        prevButton.setBorderPainted(false);
        prevButton.setContentAreaFilled(false);
        prevButton.addActionListener(e -> Player.skip());
        titlebar.add(prevButton);

        playButton = new JButton(PLAY_ICON);
        playButton.setFocusable(false);
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.addActionListener(e -> Player.playFirst());
        titlebar.add(playButton);

        JButton nextButton = new JButton(getIcon("icons/next.png", MEDIA_BUTTON_SIZE));
        nextButton.setFocusable(false);
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.addActionListener(e -> Player.skip());
        titlebar.add(nextButton);

        titlebar.add(Box.createRigidArea(DEFAULT_HORIZONTAL_DIM));

        musicTitle = new JTextField();
        musicTitle.setFocusable(false);
        musicTitle.setBorder(null);
        musicTitle.setBackground(titlebar.getBackground());
        musicTitle.setEditable(false);
        musicTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        titlebar.add(musicTitle);

        titlebar.add(Box.createRigidArea(DEFAULT_HORIZONTAL_DIM));

        JButton exitButton = new JButton (getIcon("icons/close2.png", BUTTON_SIZE));
        exitButton.setFocusable(false);
        exitButton.setSize(new Dimension(20, 20));
        exitButton.addActionListener(e -> exit(0));
        titlebar.add(exitButton);
    }

    private static ImageIcon getIcon(String path, int size) {
        return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH));
    }

    public static void setPlayIcon(boolean pause) {
        playButton.setIcon(pause ? PAUSE_ICON : PLAY_ICON);
    }

    public static void setMusicTitle(String title) {
        if (title.length() > MUSIC_TITLE_CHAR_LIMIT) musicTitle.setText(title.substring(0, MUSIC_TITLE_CHAR_LIMIT) + "...");
        else musicTitle.setText(title);
    }

    public static void main(String[] args) {
        Auth.init();
        initialize();
        frame.setVisible(true);
    }
}