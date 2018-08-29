package com.cactiCouncil.IntelliJDroplet;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

public class DropletEditor {

    public Browser browser = new Browser();
    public BrowserView view = new BrowserView(browser);

    public DropletEditor() {
        browser.loadURL("https://www.youtube.com/embed/SNvC6I1eckU?__ref=vk.kate_mobile");
    }

}
