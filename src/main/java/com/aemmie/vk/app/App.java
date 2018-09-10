package com.aemmie.vk.app;

import com.aemmie.vk.auth.Auth;
import com.aemmie.vk.music.Player;
import com.aemmie.vk.options.AppOptions;
import com.cactiCouncil.IntelliJDroplet.WinRegistry;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

import javax.swing.*;
import java.awt.*;

public class App {
    private static Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static JFrame frame;

    public static AppOptions options = AppOptions.load();

    static JPanel mainPanel = new JPanel();

    private static void initialize() {
        frameInit();

        new Thread(Player::init).start();

        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(false);
        keyboardHook.addKeyListener(new GlobalKeyAdapter() {
            @Override
            public void keyReleased(GlobalKeyEvent event) {
                switch (event.getVirtualKeyCode()) {
                    case 176: //media_next
                        TopAudioPanel.audioNext();
                        break;
                    case 177: //media_prev
                        TopAudioPanel.audioPrev();
                        break;
                    case 179: //media_play
                        TopAudioPanel.audioPlayPause();
                        break;
                }
            }
        });

    }

    private static void frameInit() {
        frame = new JFrame();
        frame.add(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(new TitleBar());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        TitleBar.setTab(2);

        frame.setIconImage(new ImageIcon("icons/vk2.png").getImage());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("VK client");
        frame.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.height -= Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration()).bottom;

        screenSize.width++;
        frame.setSize(screenSize);
    }


    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(App::exit));
        Auth.init();
        //vlcInit();
        SwingUtilities.invokeLater(() -> {
            initialize();
            frame.setVisible(true);
        });
    }



    static void exit() {
        //vlcExit();
        Player.options.save();
        Runtime.getRuntime().halt(0);
    }

    private static void vlcInit() {
        try {
            String vlcPath = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\VideoLAN\\VLC", "InstallDir");
            if (vlcPath != null) {
                if (WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\VideoLAN\\VLC", "vk_path") == null) {
                    WinRegistry.writeStringValue(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\VideoLAN\\VLC", "vk_path", vlcPath);
                }
            } else {
                WinRegistry.createKey(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\VideoLAN");
                WinRegistry.createKey(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\VideoLAN\\VLC");
            }
            WinRegistry.writeStringValue(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\VideoLAN\\VLC", "InstallDir", System.getProperty("user.dir") + "\\lib\\VLC");
            new NativeDiscovery().discover();
        } catch (Exception e) {
            LOGGER.error("VLC INIT ERROR", e);
        }
    }

    private static void vlcExit() {
        try {
            String vlcPath = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\VideoLAN\\VLC", "vk_path");
            if (vlcPath != null) {
                WinRegistry.writeStringValue(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\VideoLAN\\VLC", "InstallDir", vlcPath);
            } else WinRegistry.deleteKey(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\VideoLAN");
        } catch (Exception e) {
            LOGGER.error("VLC EXIT ERROR", e);
        }
    }
}



