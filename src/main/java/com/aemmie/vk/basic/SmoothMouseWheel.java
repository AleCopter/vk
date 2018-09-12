package com.aemmie.vk.basic;

import com.aemmie.vk.app.App;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Timer;
import java.util.TimerTask;

public class SmoothMouseWheel implements MouseWheelListener {
    private int i = 0;
    private Timer timer;
    private JScrollBar scrollBar;

    public SmoothMouseWheel(JScrollPane scrollPane) {
        this.scrollBar = scrollPane.getVerticalScrollBar();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        i += e.getWheelRotation() * App.options.SCROLL_RATE;

        if (timer == null) {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    int sign = Integer.signum(i);
                    SwingUtilities.invokeLater(() -> scrollBar.setValue(scrollBar.getValue() + sign * 5));
                    i -= sign;
                    if (i == 0) {
                        this.cancel();
                        timer = null;
                    }
                }
            }, 0, 10);
        }
    }
}
