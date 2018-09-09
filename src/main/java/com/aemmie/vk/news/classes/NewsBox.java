package com.aemmie.vk.news.classes;

import com.aemmie.vk.app.App;
import com.aemmie.vk.basic.Doc;
import com.aemmie.vk.basic.PhotoSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NewsBox extends JPanel {

    private static Logger LOGGER = LoggerFactory.getLogger(NewsBox.class);

    private static Dimension defaultSpace = new Dimension(App.options.NEWS_WIDTH, 5);

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

            if (post.text != null && !post.text.equals("")) {
                mainPanel.add(new PostText(post));
                mainPanel.add(Box.createRigidArea(new Dimension(App.options.NEWS_WIDTH, 5)));
            }

            if (post.photoList != null) { //region
                JLabel label = new JLabel();
                label.setAlignmentX(box.getAlignmentX());

                if (post.photoList.size() > 1) {
                    final Integer[] active = {0};

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setBackground(mainPanel.getBackground());
                    buttonPanel.setLayout(new GridLayout(1, 0));
                    buttonPanel.setMaximumSize(new Dimension(App.options.NEWS_WIDTH, 20));
                    mainPanel.add(buttonPanel);
                    mainPanel.add(Box.createRigidArea(defaultSpace));

                    List<ImageIcon> imageCache = new ArrayList<>();

                    for (int i = 0; i < post.photoList.size(); i++) {
                        JToggleButton button = new JToggleButton(String.valueOf(i + 1));
                        button.setFocusable(false);
                        button.setMargin(new Insets(0, 0, 0, 0));
                        if (App.options.PRELOAD_MULTI_PHOTO) {
                            imageCache.add(getScaledImage(getUrlFromPhotoList(post.photoList.get(i).sizes)));
                        }
                        button.addActionListener(new ActionListener() { //region
                            boolean preload = App.options.PRELOAD_MULTI_PHOTO;
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int a = Integer.parseInt(button.getText()) - 1;
                                active[0] = a;

                                if (preload) label.setIcon(imageCache.get(active[0]));
                                else setImage(label, getUrlFromPhotoList(post.photoList.get(active[0]).sizes));

                                toggleButton(buttonPanel, a);
                            }
                        }); //endregion
                        buttonPanel.add(button);
                    }

                    label.addMouseListener(new MouseAdapter() { //region
                        boolean preload = App.options.PRELOAD_MULTI_PHOTO;
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            active[0] += e.getX() > label.getIcon().getIconWidth() / 2 ? +1 : -1;
                            active[0] = active[0] % post.photoList.size();
                            if (active[0] < 0) active[0] = post.photoList.size() - 1;

                            if (preload) label.setIcon(imageCache.get(active[0]));
                            else setImage(label, getUrlFromPhotoList(post.photoList.get(active[0]).sizes));

                            toggleButton(buttonPanel, active[0]);
                        }
                    }); //endregion

                    toggleButton(buttonPanel, 0);
                }

                setImage(label, getUrlFromPhotoList(post.photoList.get(0).sizes));

                mainPanel.add(label);
                mainPanel.add(Box.createRigidArea(defaultSpace));
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
                            if (played) {
                                image.setIcon(staticIcon);
                            } else {
                                setImageGif(image, doc.url, width, height);
                            }
                            played = !played;
                        }
                    });
                    image.setAlignmentX(box.getAlignmentX());
                    mainPanel.add(image);
                    mainPanel.add(Box.createRigidArea(defaultSpace));
                }
                //endregion
            }

            if (post.videoList != null) { //region
                //new VKApiRequest("video.get").param("owner_id", "323289722").param("videos", "323289722_456244969").run();
                //how to get video link

                //endregion
            }

            if (post.audioList != null) { //region

                //endregion
            }

            //TODO: video, audio & other types


            mainPanel.add(Box.createRigidArea(defaultSpace));
            mainPanel.add(new BottomPanel(post)); //group, likes & views
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
            if ((post.views != null) && (
                    ((post.likes.count < 3) && (post.views.count > 80))
                    ||
                    ((post.likes.count < 10) && (post.views.count > 500))
                    ||
                    ((Instant.now().getEpochSecond() - post.date > 720) && (post.views.count / (post.likes.count + 1) > 80))))
                return true;
        }

        return false;
    }

    private static String getUrlFromPhotoList(List<PhotoSize> list) {
        return App.options.NEWS_MAX_QUALITY ?
                PhotoSize.getMaxQuality(list).url :
                PhotoSize.get(list, 'x').url;
    }

    private static void toggleButton(JPanel list, int n) {
        for (int i = 0; i < list.getComponentCount(); i++) {
            JToggleButton button = (JToggleButton) list.getComponent(i);
            button.setSelected(i == n);
        }
    }

    private static void setImage(JLabel image, String src) {
        new Thread(() -> {
            try {
                image.setIcon(getScaledImage(src));
            } catch (IOException ignored) { }
        }).start();
    }

    private static void setImage(JLabel image, BufferedImage src) {
        new Thread(() -> image.setIcon(getScaledImage(src))).start();
    }

    private static void setImageGif(JLabel image, String src, int width, int height) {
        new Thread(() -> {
            try {
                image.setIcon(getGif(src, width, height));
            } catch (IOException ignored) { }
        }).start();
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
