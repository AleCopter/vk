package com.aemmie.vk.news.classes;

import com.aemmie.vk.app.App;
import com.aemmie.vk.basic.Doc;
import com.aemmie.vk.basic.Group;
import com.aemmie.vk.basic.PhotoSize;
import com.aemmie.vk.news.NewsApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class NewsBox extends JPanel {

    private static Logger LOGGER = LoggerFactory.getLogger(NewsBox.class);

    private NewsBox() {
        super();
    }

    public static NewsBox create(Post post) {
        if (isBadPost(post)) {
            LOGGER.info("SKIPPED: \n" + post.text);
            return null;
        }

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
                        JToggleButton button = new JToggleButton(String.valueOf(i + 1));
                        button.setFocusable(false);
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
                            } catch (Exception ignored) {
                            }
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
                            } catch (Exception ignored) {
                            }
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
                mainPanel.add(Box.createRigidArea(new Dimension(App.options.NEWS_WIDTH, 5)));
                //endregion
            }

            if (post.docList != null) { //region
                for (Doc doc : post.docList) {
                    BufferedImage bufferedImage = ImageIO.read(new URL(PhotoSize.get(doc.sizes, 'o').src));
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

                    int width = watermarked.getWidth();
                    int height = watermarked.getHeight();

                    JLabel image = new JLabel(staticIcon);

                    image.addMouseListener(new MouseAdapter() {
                        boolean played = false;

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            try {
                                image.setIcon(played ? staticIcon : getGif((doc).url, width, height));
                            } catch (Exception ignored) { }
                            played = !played;
                        }
                    });
                    image.setAlignmentX(box.getAlignmentX());
                    mainPanel.add(image);
                    mainPanel.add(Box.createRigidArea(new Dimension(App.options.NEWS_WIDTH, 5)));
                }
                //endregion
            }

            //TODO: other types


            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new GridLayout(1, 0));
            bottomPanel.setMaximumSize(new Dimension(App.options.NEWS_WIDTH, 30));
            bottomPanel.setBackground(Color.WHITE);
            if (post.source_id < 0) {
                Group group = NewsApi.groups.get(-1 * post.source_id);
                JLabel groupImage = new JLabel(group.image, SwingConstants.LEFT);

                groupImage.setText(group.name);
                groupImage.setBorder(null);
                groupImage.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

                bottomPanel.add(groupImage);
            }

            String info = "❤ " + post.likes.count;
            if (post.views != null) info += "   \uD83D\uDC41 " + post.views.count;
            JLabel right = new JLabel(info, SwingConstants.RIGHT);
            right.setAlignmentX(RIGHT_ALIGNMENT);
            right.setBorder(null);
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

    private static boolean isBadPost(Post post) {

        if (App.options.NEWS_TEXT_FILTER && post.text != null) {
            String[] words = App.options.TEXT_FILTER.split(",");
            if (Arrays.stream(words).parallel().anyMatch(post.text::contains)) return true;
        }

        if (App.options.NEWS_LIKE_FILTER) {
            if (post.views != null && post.views.count / (post.likes.count + 1) > 80) return true;
        }

        return false;
    }

    private static void toggleButton(JPanel list, int n) {
        for (int i = 0; i < list.getComponentCount(); i++) {
            JToggleButton button = (JToggleButton) list.getComponent(i);
            button.setSelected(i == n);
        }
    }

    private static ImageIcon getScaledImage(String src) throws IOException {
        BufferedImage img = ImageIO.read(new URL(src));
        return getScaledImage(img);
    }

    private static ImageIcon getScaledImage(BufferedImage src) {
        if (src.getHeight() * App.options.NEWS_WIDTH / src.getWidth() > App.options.NEWS_HEIGHT) {
            return new ImageIcon(src.getScaledInstance(-1, App.options.NEWS_HEIGHT, Image.SCALE_SMOOTH));
        } else {
            return new ImageIcon(src.getScaledInstance(App.options.NEWS_WIDTH, -1, Image.SCALE_SMOOTH));
        }
    }

    private static ImageIcon getGif(String src, int width, int height) throws MalformedURLException {
        if (height * App.options.NEWS_WIDTH / width > App.options.NEWS_HEIGHT) {
            return new ImageIcon(new ImageIcon(new URL(src)).getImage().getScaledInstance(-1, App.options.NEWS_HEIGHT, Image.SCALE_FAST));
        } else {
            return new ImageIcon(new ImageIcon(new URL(src)).getImage().getScaledInstance(App.options.NEWS_WIDTH, -1, Image.SCALE_FAST));
        }
    }

}
