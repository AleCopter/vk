package com.aemmie.vk.news.classes;

import com.aemmie.vk.app.App;

import javax.swing.*;
import java.awt.*;

public class PostText extends JTextArea {

    PostText(Post post) {
        super(post.text);
        this.setEditable(false);
        this.setMaximumSize(new Dimension(App.options.NEWS_WIDTH, 0));
        //this.setAlignmentX(box.getAlignmentX());
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
    }
}
