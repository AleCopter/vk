package com.aemmie.vk.app;

import com.aemmie.vk.core.Tab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class OptionsTab extends Tab {
    private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    private JPanel panel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane(panel);

    private JPanel topPanel = new JPanel();

    private Options options;

    private Dimension buttonsSize = new Dimension(100, 40);

    public OptionsTab() {
        reloadOptions();

        panel.setBackground(Color.DARK_GRAY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(null);

        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(App.options.SCROLL_RATE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.add(scrollPane);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(null);

        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        //topPanel.setLayout(new GridLayout(1, 0));
        JButton loadDefault = new JButton("default");
        loadDefault.setFocusable(false);
        loadDefault.addActionListener(e -> {
            options = new Options();
            createOptions();
        });
        //loadDefault.setMaximumSize(buttonsSize);
        topPanel.add(loadDefault);

        JButton cancel = new JButton("cancel");
        cancel.setFocusable(false);
        cancel.addActionListener(e -> {
            reloadOptions();
            createOptions();
        });
        //cancel.setMaximumSize(buttonsSize);
        topPanel.add(cancel);

        JButton save = new JButton("save");
        save.setFocusable(false);
        save.addActionListener(e -> {
            options.save();
            App.options = options;
        });
        //save.setMaximumSize(buttonsSize);
        topPanel.add(save);

        createOptions();
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

    private void createOptions() {
        panel.removeAll();
        panel.add(createIntegerOption("test width", "NEWS_WIDTH", 300, 700));

        panel.updateUI();
    }

    private void reloadOptions() {
        options = Options.load();
    }

    private JPanel createIntegerOption(String name, String var, int left, int right) {
        try {
            Field field = Options.class.getField(var);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.setBackground(Color.DARK_GRAY);

            JTextField text = new JTextField(name);
            text.setEditable(false);
            text.setBorder(null);
            text.setBackground(Color.WHITE);
            text.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
            text.setMaximumSize(new Dimension(500, 20));
            panel.add(text);

            JSlider slider = new JSlider(JSlider.HORIZONTAL, left, right, (int)field.get(options));
            slider.addChangeListener(e -> {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    try {
                        field.set(options, source.getValue());
                    } catch (Exception ignored) { }
                }
            });
            panel.add(slider);
            return panel;
        } catch (Exception e) {
            LOGGER.error("WRONG FIELD " + var, e);
        }
        return null;
    }
}
