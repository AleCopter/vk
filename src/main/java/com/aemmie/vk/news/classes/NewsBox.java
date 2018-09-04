package com.aemmie.vk.news.classes;

import com.aemmie.vk.app.App;
import com.aemmie.vk.basic.Group;
import com.aemmie.vk.basic.PhotoSize;
import com.aemmie.vk.news.NewsApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class NewsBox extends JPanel {

    private static Logger LOGGER = LoggerFactory.getLogger(NewsBox.class);

    private NewsBox() {
        super();
    }

    public static NewsBox create(Post post) {
        if (check(post)) return null;

        NewsBox box = new NewsBox();

        try {
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
            box.setBackground(Color.WHITE);
            box.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(new LineBorder(Color.WHITE, 10));
            mainPanel.setBackground(Color.WHITE);
            box.setBackground(Color.DARK_GRAY);

            if (post.text != null && !post.text.equals("")) { //region
                JTextArea text = new JTextArea(post.text);
                text.setEditable(false);
                text.setMaximumSize(new Dimension(App.options.NEWS_WIDTH, 0));
                text.setAlignmentX(box.getAlignmentX());
                text.setLineWrap(true);
                text.setWrapStyleWord(true);
                text.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                mainPanel.add(text);
                mainPanel.add(Box.createRigidArea(new Dimension(App.options.NEWS_WIDTH, 5)));
                //endregion
            }

            if (post.photoList != null) { //region
                JLabel label = new JLabel();
                label.setAlignmentX(box.getAlignmentX());

                //TODO: add option for preload multiple images
                if (post.photoList.size() > 1) {
                    final Integer[] active = {0};

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setBackground(mainPanel.getBackground());
                    buttonPanel.setLayout(new GridLayout(1, 0));
                    Dimension dim = new Dimension(App.options.NEWS_WIDTH, 20);
                    buttonPanel.setMaximumSize(dim);
                    mainPanel.add(buttonPanel);
                    mainPanel.add(Box.createRigidArea(new Dimension(App.options.NEWS_WIDTH, 5)));

                    for (int i = 0; i < post.photoList.size(); i++) {
                        JToggleButton button = new JToggleButton(String.valueOf(i+1));
                        button.setMargin(new Insets(0, 0, 0, 0));
                        button.addActionListener(e -> {
                            int a = Integer.parseInt(button.getText()) - 1;
                            active[0] = a;
                            List<PhotoSize> sizes = post.photoList.get(active[0] % post.photoList.size()).sizes;
                            ImageIcon image = null;
                            try {
                                image = getScaledImage(App.options.NEWS_MAX_QUALITY ?
                                        PhotoSize.getMaxQuality(sizes).url :
                                        PhotoSize.get(sizes, 'x').url);
                            } catch (MalformedURLException ignored) { }
                            label.setIcon(image);
                            toggleButton(buttonPanel, a);
                        });
                        buttonPanel.add(button);
                    }

                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            active[0] += e.getX() > label.getIcon().getIconWidth() / 2 ? +1 : -1;
                            active[0] = active[0] % post.photoList.size();
                            if (active[0] < 0) active[0] = post.photoList.size() - 1;

                            List<PhotoSize> sizes = post.photoList.get(active[0]).sizes;
                            ImageIcon image = null;
                            try {
                                image = getScaledImage(App.options.NEWS_MAX_QUALITY ?
                                        PhotoSize.getMaxQuality(sizes).url :
                                        PhotoSize.get(sizes, 'x').url);
                            } catch (MalformedURLException ignored) { }
                            label.setIcon(image);
                            toggleButton(buttonPanel, active[0]);
                        }
                    });

                    toggleButton(buttonPanel, 0);
                }

                ImageIcon image = getScaledImage(App.options.NEWS_MAX_QUALITY ?
                        PhotoSize.getMaxQuality(post.photoList.get(0).sizes).url :
                        PhotoSize.get(post.photoList.get(0).sizes, 'x').url);
                label.setIcon(image);

                mainPanel.add(label);

                //endregion
            }

            //TODO: other types


            //region
            /*
            for (Pair<String, Object> pair : post.attachmentsList) {
                try {
                    switch (pair.getKey()) {
                        case "photo": {
                            ImageIcon image = getScaledImage(App.options.NEWS_MAX_QUALITY ?
                                    PhotoSize.getMaxQuality(((Photo) pair.getValue()).sizes).url :
                                    PhotoSize.get(((Photo) pair.getValue()).sizes, 'x').url);
                            photoList.add(image);
                            if (mainImage == null) {
                                mainImage = new JLabel(image);
                                mainImage.setAlignmentX(box.getAlignmentX());
                                if (post.attachmentsList.size() > 1 && post.attachmentsList.get(1).getKey().equals("photo")) {
                                    mainPanel.add(Box.createRigidArea(new Dimension(App.options.NEWS_WIDTH, 10)));
                                    JLabel finalMainImage = mainImage;
                                    mainImage.addMouseListener(new MouseAdapter() {
                                        @Override
                                        public void mouseReleased(MouseEvent e) {
                                            active[0] += e.getX() > finalMainImage.getIcon().getIconWidth() / 2 ? +1 : -1;
                                            if (active[0] < 0) active[0] = photoList.size() - 1;
                                            finalMainImage.setIcon(photoList.get(active[0] % photoList.size()));
                                            toggleButton(buttonList, active[0]);
                                        }
                                    });
                                }
                                mainPanel.add(mainImage);
                            }

                            JToggleButton button = new JToggleButton(String.valueOf(photoList.size()));
                            button.setFocusable(false);
                            buttonList.add(button);
                            JLabel finalMainImage1 = mainImage;
                            button.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    int a = Integer.parseInt(button.getText()) - 1;
                                    active[0] = a;
                                    finalMainImage1.setIcon(photoList.get(a));
                                    toggleButton(buttonList, a);
                                }
                            });
                            break;
                        }
                        case "doc": {
                            BufferedImage bufferedImage = ImageIO.read(new URL(PhotoSize.get(((Doc) (pair.getValue())).sizes, 'o').src));
                            String text = "►";
                            int imageType = bufferedImage.getType();
                            BufferedImage watermarked = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), imageType);
                            Graphics2D w = (Graphics2D) watermarked.getGraphics();
                            w.drawImage(bufferedImage, 0, 0, null);
                            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
                            w.setComposite(alphaChannel);
                            w.setColor(Color.WHITE);
                            w.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 150));
                            FontMetrics fontMetrics = w.getFontMetrics();
                            Rectangle2D rect = fontMetrics.getStringBounds(text, w);
                            int centerX = (bufferedImage.getWidth() - (int) rect.getWidth()) / 2;
                            int centerY = (bufferedImage.getHeight() + (int) rect.getHeight() / 2) / 2;
                            w.drawString(text, centerX, centerY);

                            ImageIcon staticIcon = getScaledImage(watermarked);

                            JLabel image = new JLabel(staticIcon);

                            image.addMouseListener(new MouseAdapter() {
                                boolean played = false;

                                @Override
                                public void mouseReleased(MouseEvent e) {
                                    try {
                                        image.setIcon(played ? staticIcon : getDefaultScaledImage(((Doc) (pair.getValue())).url));
                                    } catch (MalformedURLException e1) {
                                        LOGGER.error("GIF ERROR:", e);
                                    }
                                    played = !played;
                                }
                            });
                            image.setAlignmentX(box.getAlignmentX());
                            mainPanel.add(Box.createRigidArea(new Dimension(App.options.NEWS_WIDTH, 10)));
                            mainPanel.add(image);
                            break;
                        }
                        default:
                            mainPanel.add(Box.createRigidArea(new Dimension(App.options.NEWS_WIDTH, 10)));
                            JTextArea def = new JTextArea("-[" + pair.getKey() + "]- not supported");
                            def.setEditable(false);
                            def.setMaximumSize(new Dimension(App.options.NEWS_WIDTH, 0));
                            def.setAlignmentX(box.getAlignmentX());
                            def.setLineWrap(true);
                            def.setWrapStyleWord(true);
                            mainPanel.add(def);
                            break;
                    }
                } catch (Exception e) {
                    LOGGER.error("NEWSBOX INIT EXCEPTION:", e);
                }
            }

            if (photoList.size() > 1) {
                for (JToggleButton button : buttonList) {
                    buttonPanel.add(button);
                }
                buttonPanel.setMinimumSize(new Dimension(App.options.NEWS_WIDTH, 20));
                buttonPanel.setVisible(true);
                buttonList.get(0).setSelected(true);
            }
            */
            //endregion

            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
            bottomPanel.setMaximumSize(new Dimension(App.options.NEWS_WIDTH, 20));
            bottomPanel.setBackground(Color.WHITE);
            if (post.source_id < 0) {
                Group group = NewsApi.groups.get(-1 * post.source_id);
                JLabel groupImage = new JLabel(group.image);

                JTextField groupName = new JTextField("   " + group.name);
                groupName.setEditable(false);
                groupName.setBorder(null);
                groupName.setBackground(Color.WHITE);
                groupName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

                bottomPanel.add(groupImage);
                bottomPanel.add(groupName);
            }

            String info = "❤ " + post.likes.count;
            if (post.views != null) info += "   \uD83D\uDC41 " + post.views.count;
            JTextField right = new JTextField(info);
            right.setAlignmentX(RIGHT_ALIGNMENT);
            right.setEditable(false);
            right.setHorizontalAlignment(SwingConstants.RIGHT);
            right.setBorder(null);
            right.setBackground(Color.WHITE);
            right.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            bottomPanel.add(right);

            mainPanel.add(Box.createRigidArea(new Dimension(App.options.NEWS_WIDTH, 10)));
            mainPanel.add(bottomPanel);

            mainPanel.add(Box.createVerticalGlue());

            box.add(mainPanel);
            box.add(Box.createRigidArea(new Dimension(0, 30)));
            return box;
        } catch (Exception e) {
            LOGGER.error("POST INIT ERROR", e);
            return null;
        }
    }

    private static boolean check(Post post) {

        return false;
    }

    private static void toggleButton(JPanel list, int n) {
        for (int i = 0; i < list.getComponentCount(); i++) {
            JToggleButton button = (JToggleButton)list.getComponent(i);
            button.setSelected(i == n);
        }
    }

    private static ImageIcon getScaledImage(String src) throws MalformedURLException {
        return new ImageIcon(new ImageIcon(new URL(src)).getImage().getScaledInstance(App.options.NEWS_WIDTH, -1, Image.SCALE_SMOOTH));
    }

    private static ImageIcon getScaledImage(BufferedImage src) {
        return new ImageIcon(new ImageIcon(src).getImage().getScaledInstance(App.options.NEWS_WIDTH, -1, Image.SCALE_SMOOTH));
    }

    private static ImageIcon getDefaultScaledImage(String src) throws MalformedURLException {
        return new ImageIcon(new ImageIcon(new URL(src)).getImage().getScaledInstance(App.options.NEWS_WIDTH, -1, Image.SCALE_DEFAULT));
    }

}
