package com.aemmie.vk.news.classes;

import com.aemmie.vk.app.App;
import com.aemmie.vk.data.Group;
import com.aemmie.vk.news.NewsApi;

import javax.swing.*;
import java.awt.*;

class BottomPanel extends JPanel {

    BottomPanel(Post post) {
        this.setLayout(new GridLayout(1, 0));
        this.setMaximumSize(new Dimension(App.options.NEWS_WIDTH, 30));
        this.setBackground(Color.WHITE);

        if (post.source_id < 0) {
            Group group = NewsApi.groups.get(-1 * post.source_id);
            JLabel groupImage = new JLabel(group.image, SwingConstants.LEFT);

            groupImage.setText(group.name);
            groupImage.setBorder(null);
            groupImage.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

            this.add(groupImage);
        }

        String info = "❤ " + post.likes.count;
        if (post.views != null) info += "   \uD83D\uDC41 " + post.views.count;
        JLabel right = new JLabel(info, SwingConstants.RIGHT);
        right.setAlignmentX(RIGHT_ALIGNMENT);
        right.setBorder(null);
        right.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        this.add(right);
    }
}
