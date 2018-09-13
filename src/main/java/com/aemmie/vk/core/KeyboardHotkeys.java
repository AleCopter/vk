package com.aemmie.vk.core;

import com.aemmie.vk.app.titlebar.TopAudioPanel;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class KeyboardHotkeys {
    private static GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(false);

    static {
        keyboardHook.addKeyListener(new GlobalKeyAdapter() {
            @Override
            public void keyReleased(GlobalKeyEvent event) {
                switch (event.getVirtualKeyCode()) {
                    case 176: //media_next
                        TopAudioPanel.audioNext();
                        break;
                    case 177: //media_prev
                        TopAudioPanel.audioPrev();
                        break;
                    case 179: //media_play
                        TopAudioPanel.audioPlayPause();
                        break;
                }
            }
        });
    }

    public static void init() {}
}
