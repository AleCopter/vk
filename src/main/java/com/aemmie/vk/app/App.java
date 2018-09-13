package com.aemmie.vk.app;

import com.aemmie.vk.app.tabs.Tab;
import com.aemmie.vk.app.titlebar.TitleBar;
import com.aemmie.vk.core.Auth;
import com.aemmie.vk.core.Global;
import com.aemmie.vk.core.KeyboardHotkeys;
import com.aemmie.vk.music.Player;
import com.aemmie.vk.options.AppOptions;
import com.cactiCouncil.IntelliJDroplet.WinRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.binding.LibC;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class App {
    private static Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static JFrame frame;

    public static AppOptions options = AppOptions.load();

    private static JPanel mainPanel = new JPanel();

    private static void initialize() {
        frame = new JFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            Global.background = ImageIO.read(new URL("https://images.wallpaperscraft.ru/image/zvezdy_kosmos_siyanie_planeta_99744_1920x1080.jpg"))
                    .getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
        } catch (IOException ignored) { }
        screenSize.height -= Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration()).bottom;
        screenSize.width++;
        Global.screenSize = screenSize;
        frame.setSize(screenSize);


        frame.setContentPane(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(new TitleBar());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        TitleBar.setTab(2);

        frame.setIconImage(new ImageIcon("icons/vk2.png").getImage());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("VK client");
        frame.setUndecorated(true);

        KeyboardHotkeys.init();
        new Thread(Player::init).start();
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(App::exit));
        Auth.init();
        vlcInit();
        SwingUtilities.invokeLater(() -> {
            initialize();
            frame.setVisible(true);
        });
    }

    public static void setMainPanel(Tab tab) {
        mainPanel.remove(1);
        mainPanel.add(tab);
        mainPanel.updateUI();
    }

    private static void exit() {
        vlcExit();
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
            LibC.INSTANCE._putenv("VLC_PLUGIN_PATH=" + System.getProperty("user.dir") + "\\lib\\VLC");
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



