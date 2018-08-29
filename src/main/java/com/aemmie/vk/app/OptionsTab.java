package com.aemmie.vk.app;

import com.aemmie.vk.core.Tab;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsTab extends Tab {

    private JPanel panel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane(panel);

    private JPanel topPanel = new JPanel();

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

        panel.add(new SwingFXWebView());
    }

    @Override
    public void init() {

    }

    @Override
    public JPanel getTopPanel() {
        return topPanel;
    }
}

class SwingFXWebView extends JPanel {

    private Stage stage;
    private WebView browser;
    private JFXPanel jfxPanel;
    private JButton swingButton;
    private WebEngine webEngine;

    public SwingFXWebView(){
        initComponents();
    }

    public static void main(String ...args){
        // Run this later:
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JFrame frame = new JFrame();

                frame.getContentPane().add(new SwingFXWebView());

                frame.setMinimumSize(new Dimension(640, 480));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

    private void initComponents(){

        jfxPanel = new JFXPanel();
        createScene();

        setLayout(new BorderLayout());
        add(jfxPanel, BorderLayout.CENTER);

        swingButton = new JButton();
        swingButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        webEngine.reload();
                    }
                });
            }
        });
        swingButton.setText("Reload");

        add(swingButton, BorderLayout.SOUTH);
    }

    private void createScene() {
        PlatformImpl.startup(new Runnable() {
            @Override
            public void run() {

                stage = new Stage();

                stage.setTitle("Hello Java FX");
                stage.setResizable(true);

                Group root = new Group();
                Scene scene = new Scene(root,80,20);
                stage.setScene(scene);

                // Set up the embedded browser:
                browser = new WebView();
                webEngine = browser.getEngine();
                webEngine.load("https://www.youtube.com/embed/SNvC6I1eckU?__ref=vk.kate_mobile");

                ObservableList<Node> children = root.getChildren();
                children.add(browser);

                jfxPanel.setScene(scene);
            }
        });
    }
}