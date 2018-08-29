package com.aemmie.vk.app;

import com.aemmie.vk.auth.Auth;
import com.aemmie.vk.core.Tab;
import com.aemmie.vk.music.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class App {
    private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static JFrame frame;

    private static JPanel titlebar = new JPanel();
    private static JPanel mainPanel = new JPanel();


    private static ArrayList<Tab> tabsList = new ArrayList<>();
    private static ArrayList<JToggleButton> tabButtonsList = new ArrayList<>();

    private static JButton playButton;

    private static JTextField musicTitle;

    private static int titlebar_instrument_id;

    private static final int BUTTON_SIZE = 40;
    private static final int MEDIA_BUTTON_SIZE = 25;
    private static final int MUSIC_TITLE_CHAR_LIMIT = 60;

    private static final Dimension DEFAULT_HORIZONTAL_DIM = new Dimension(20, 0);

    private static final ImageIcon PLAY_ICON = getIcon("icons/play.png", MEDIA_BUTTON_SIZE);
    private static final ImageIcon PAUSE_ICON = getIcon("icons/pause.png", MEDIA_BUTTON_SIZE);

    private static void initialize() {
        frameInit();

        //tabsList.get(0).init();

        //Player.setAudioList(Auth.getMyId());
        //new VKApiRequest("video.get").param("owner_id", "323289722").param("videos", "323289722_456244969").run();
    }

    private static void frameInit() {
        frame = new JFrame();
        frame.add(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        titlebarInit();

        mainPanel.add(titlebar);
        mainPanel.add(tabsList.get(0));
        setTab(tabButtonsList.get(1));

        frame.setIconImage(new ImageIcon("icons/vk2.png").getImage());
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                App.exit(0);
            }
        });
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

        createNewTab("news.png", new NewsTab());
        //createNewTab("messages.png", null);
        //createNewTab("music.png", null);
        createNewTab("options.png", new OptionsTab());

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
        titlebar_instrument_id = titlebar.getComponentCount();
        titlebar.add(Box.createRigidArea(DEFAULT_HORIZONTAL_DIM));
        titlebar.add(Box.createRigidArea(DEFAULT_HORIZONTAL_DIM));

        JButton exitButton = new JButton(getIcon("icons/close2.png", BUTTON_SIZE));
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
        if (title.length() > MUSIC_TITLE_CHAR_LIMIT)
            musicTitle.setText(title.substring(0, MUSIC_TITLE_CHAR_LIMIT) + "...");
        else musicTitle.setText(title);
    }

    public static void main(String[] args) {
        Auth.init();
        SwingUtilities.invokeLater(() -> {
            new NativeDiscovery().discover();
            initialize();
            frame.setVisible(true);
            ((OptionsTab) tabsList.get(1)).play();
        });
    }

    private static void createNewTab(String icon, Tab tab) {
        JToggleButton button = new JToggleButton(getIcon("icons/" + icon, BUTTON_SIZE));
        button.setFocusable(false);
        button.setSelected(false);
        button.addActionListener(e -> setTab((JToggleButton) e.getSource()));
        titlebar.add(button);

        tabButtonsList.add(button);
        tabsList.add(tab);
    }

    private static void setTab(JToggleButton tabButton) {
        mainPanel.remove(1);
        Tab tabFromList = tabsList.get(tabButtonsList.indexOf(tabButton));
        mainPanel.add(tabFromList);
        mainPanel.updateUI();
        tabFromList.onUpdate();

        titlebar.remove(titlebar_instrument_id);
        titlebar.add(tabFromList.getTopPanel(), titlebar_instrument_id);
        tabButtonsList.forEach(jToggleButton -> jToggleButton.setSelected(false));
        tabButton.setSelected(true);
    }

    private static void exit(int code) {
        //
        System.exit(code);
    }
}



