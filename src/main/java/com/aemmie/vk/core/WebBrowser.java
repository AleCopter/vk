package com.aemmie.vk.core;

import com.cactiCouncil.IntelliJDroplet.DropletEditor;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import java.awt.*;


public class WebBrowser {
    private DropletEditor c = new DropletEditor();

    public BrowserView add() {
        return c.view;
    }

    public WebBrowser setURL(String url) {
        c.browser.loadURL(url);
        c.view.setAlignmentX(Component.CENTER_ALIGNMENT);
//        c.view.setMaximumSize(new Dimension(510, 340));
//        c.view.setMinimumSize(new Dimension(510, 340));
        return this;
    }

    public Browser get() {
        return c.browser;
    }
}
