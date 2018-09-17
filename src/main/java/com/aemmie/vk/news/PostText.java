package com.aemmie.vk.news;

import com.aemmie.vk.app.App;
import com.aemmie.vk.data.Post;

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
