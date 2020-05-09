package com.thingtek.view.component.panel;

import javax.swing.*;
import java.awt.*;

/*
        渐变面板
 */
public class ShadowPanel extends JPanel {

    private Color c1 = null, c2 = null;// 背景色
    private Image image;// 背景图片
    private float alpha;// 0.0~1.0f透明度

    // 图片背景
    public ShadowPanel(Image image, float alpha) {
        this.alpha = alpha;
        this.image = image;
        this.init();
    }

    private void init() {
        this.setOpaque(false);// 很必要
        // 希望它大小是自我调整的
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setSize(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        AlphaComposite composite = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, alpha);
        g2.setComposite(composite);

        if (image != null) {
            g2.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        }

        if (c1 != null) {
            if (c2 != null) {
                g2.setPaint(new GradientPaint(0, 0, c1, getWidth(),getHeight(), c2));
            } else {
                g2.setColor(c1);
            }
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        g2.dispose();
        super.paintComponent(g);

    }

    @Override
    public void setBackground(Color bg) {
        if (c1 != null) {
            this.c1 = bg;
            this.repaint();
        }
    }

}
