package com.aemmie.vk.news.classes;

import com.aemmie.vk.app.Options;
import com.aemmie.vk.basic.Doc;
import com.aemmie.vk.basic.Group;
import com.aemmie.vk.basic.Photo;
import com.aemmie.vk.basic.PhotoSize;
import com.aemmie.vk.news.NewsApi;
import javafx.util.Pair;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsBox extends JPanel {

    private static Logger LOGGER = LoggerFactory.getLogger(NewsBox.class);

    public NewsBox(Post post) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new LineBorder(Color.WHITE, 10));
        mainPanel.setBackground(Color.WHITE);
        this.setBackground(Color.DARK_GRAY);

        if (post.text != null && !post.text.equals("")) {
            JTextArea text = new JTextArea(post.text);
            text.setEditable(false);
            text.setMaximumSize(new Dimension(Options.NEWS_WIDTH, 0));
            text.setAlignmentX(this.getAlignmentX());
            text.setLineWrap(true);
            text.setWrapStyleWord(true);
            text.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            mainPanel.add(text);
            mainPanel.add(Box.createRigidArea(new Dimension(Options.NEWS_WIDTH, 5)));
        }

        ArrayList<ImageIcon> photoList = new ArrayList<>();
        ArrayList<JToggleButton> buttonList = new ArrayList<>();
        final Integer[] active = {0};
        JLabel mainImage = null;

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setVisible(false);
        mainPanel.add(buttonPanel);

        for (Pair<String, Object> pair : post.attachmentsList) {
            try {
                switch (pair.getKey()) {
                    case "photo": {
                        ImageIcon image = getScaledImage(Options.NEWS_MAX_QUALITY ?
                                PhotoSize.getMaxQuality(((Photo) pair.getValue()).sizes).url :
                                PhotoSize.get(((Photo) pair.getValue()).sizes, 'x').url);
                        photoList.add(image);
                        if (mainImage == null) {
                            mainImage = new JLabel(image);
                            mainImage.setAlignmentX(this.getAlignmentX());
                            if (post.attachmentsList.size() > 1) mainPanel.add(Box.createRigidArea(new Dimension(Options.NEWS_WIDTH, 10)));
                            mainPanel.add(mainImage);
                        } else {
                            JLabel finalMainImage = mainImage;
                            mainImage.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseReleased(MouseEvent e) {
                                    active[0] += e.getX() > finalMainImage.getIcon().getIconWidth() / 2 ? -1 : +1;
                                    if (active[0] < 0) active[0] = photoList.size() - 1;
                                    finalMainImage.setIcon(photoList.get(active[0] % photoList.size()));
                                    toggleButton(buttonList, active[0]);
                                }
                            });
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
                        image.setAlignmentX(this.getAlignmentX());
                        mainPanel.add(Box.createRigidArea(new Dimension(Options.NEWS_WIDTH, 10)));
                        mainPanel.add(image);
                        break;
                    }
                    default:
                        mainPanel.add(Box.createRigidArea(new Dimension(Options.NEWS_WIDTH, 10)));
                        JTextArea def = new JTextArea("-[" + pair.getKey() + "]- not supported");
                        def.setEditable(false);
                        def.setMaximumSize(new Dimension(Options.NEWS_WIDTH, 0));
                        def.setAlignmentX(this.getAlignmentX());
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
            buttonPanel.setMinimumSize(new Dimension(Options.NEWS_WIDTH, 20));
            buttonPanel.setVisible(true);
            buttonList.get(0).setSelected(true);
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setMaximumSize(new Dimension(Options.NEWS_WIDTH, 20));
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

        mainPanel.add(Box.createRigidArea(new Dimension(Options.NEWS_WIDTH, 10)));
        mainPanel.add(bottomPanel);

        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
        add(Box.createRigidArea(new Dimension(0, 30)));
    }

    private static void toggleButton(ArrayList<JToggleButton> list, int n) {
        for (JToggleButton button : list) {
            button.setSelected(false);
        }
        list.get(n % list.size()).setSelected(true);
    }

    private static ImageIcon getScaledImage(String src) throws MalformedURLException {
        return new ImageIcon(new ImageIcon(new URL(src)).getImage().getScaledInstance(Options.NEWS_WIDTH, -1, Image.SCALE_SMOOTH));
    }

    private static ImageIcon getDefaultScaledImage(String src) throws MalformedURLException {
        return new ImageIcon(new ImageIcon(new URL(src)).getImage().getScaledInstance(Options.NEWS_WIDTH, -1, Image.SCALE_DEFAULT));
    }

    private static ImageIcon getScaledImage(BufferedImage src) {
        return new ImageIcon(new ImageIcon(src).getImage().getScaledInstance(Options.NEWS_WIDTH, -1, Image.SCALE_SMOOTH));
    }
}
