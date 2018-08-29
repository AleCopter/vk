package com.cactiCouncil.IntelliJDroplet;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

public class DropletEditor {

    public Browser browser = new Browser(BrowserType.LIGHTWEIGHT);
    public BrowserView view = new BrowserView(browser);

}
