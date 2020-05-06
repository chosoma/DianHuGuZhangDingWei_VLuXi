package com.thingtek.view.component.panel;

import javax.swing.*;
import java.awt.*;

public class BackGroundPanel extends JPanel {

    private Image image;

    public BackGroundPanel(Image image) {
        this();
        this.image = image;
    }

    private BackGroundPanel() {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(179, 211, 240));
        this.setBorder(null);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setStroke(new BasicStroke(9f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        g2.setColor(Color.lightGray);
        if (image != null) {
            g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
        g2.dispose();
        super.paintComponent(g);
    }

}
