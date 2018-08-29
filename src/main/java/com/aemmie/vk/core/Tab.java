package com.aemmie.vk.core;

import javax.swing.*;

public abstract class Tab extends JPanel {

    public abstract void init();

    public abstract void onUpdate();

    public abstract JPanel getTopPanel();
}
