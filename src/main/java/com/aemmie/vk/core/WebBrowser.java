package com.aemmie.vk.core;

import com.cactiCouncil.IntelliJDroplet.DropletEditor;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

public class WebBrowser {
    private DropletEditor c = new DropletEditor();

    public BrowserView add() {
        return c.view;
    }

    public WebBrowser setURL(String url) {
        c.browser.loadURL(url);
        return this;
    }
}
