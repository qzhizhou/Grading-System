package UI.Bottom;

import javax.swing.*;
import java.awt.*;

/**
 * @Auther: Di Zhu
 * @Date: 04-30-2019 11:51
 * @Description: Panel to show BU logo
 */
public class ImagePanel extends JPanel {

    private Image img;

    public ImagePanel(String img) {
        this(new ImageIcon(img).getImage());
    }

    public ImagePanel(Image img) {
        img = img.getScaledInstance(128, 58, Image.SCALE_DEFAULT);
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
}
