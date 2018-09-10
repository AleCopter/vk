package com.aemmie.vk.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class TODO extends JPanel {

    TODO() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setAlignmentX(CENTER_ALIGNMENT);

        addText("TODO:");
        addText("video support");
        addText("fix gif bug");
        addText("multi-image fullsize scroll (left & right)");
        addText("top panel music buttons");
        addText("top panel right buttons");
        addText("fix right-side taskbar");

        this.updateUI();
    }

    private void addText(String text) {
        JLabel label = new JLabel(text);
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.add(label);
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(1000, 5));
        this.add(separator);
    }
}
