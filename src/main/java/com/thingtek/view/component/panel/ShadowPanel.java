package com.thingtek.view.component.panel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/*
        渐变面板
 */
public class ShadowPanel extends JPanel {

    private Color c1 = null, c2 = null;// 背景色
    private Image image = null;// 背景图片
    private float alpha = 1.0f;// 0.0~1.0f透明度
    // 渐变色方向
    public final static int HORIZONTAL = 1, VERTICAL = 2, SLANT = 3;
    private int orientation = SLANT;

    public ShadowPanel(String imageURL) {
        this(imageURL, 1.0f);
    }

    // 9宫格图片背景
    public ShadowPanel(String imageURL, float alpha) {
        this.alpha = alpha;
        try {
            image = ImageIO.read(new File(imageURL));
        } catch (IOException e) {
            e.printStackTrace();
            c1 = Color.WHITE;
        }
        this.init();
    }

    public ShadowPanel(Image image) {
        this(image, 1.0f);
    }

    // 图片背景
    public ShadowPanel(Image image, float alpha) {
        this.alpha = alpha;
        this.image = image;
        this.init();
    }

    // 单色背景
    public ShadowPanel(Color c1) {
        this(c1, 1.0f);
    }

    // 单色背景
    public ShadowPanel(Color c1, float alpha) {
        this(c1, null, alpha);
    }

    // 双色背景，上下渐变
    public ShadowPanel(Color[] colors) {
        this(colors, 1.0f);
    }

    // 双色背景，上下渐变
    public ShadowPanel(Color[] colors, float alpha) {
        this(colors[0], colors[1], alpha);
    }

    public ShadowPanel(Color c1, Color c2, float alpha) {
        this.c1 = c1;
        this.c2 = c2;
        this.alpha = alpha;
        this.init();
    }

    public ShadowPanel(int newOrientation, Color... colors) {
        this(newOrientation, 1.0f, colors[0], colors[1]);
    }

    public ShadowPanel(int newOrientation, float alpha, Color c1, Color c2) {
        this.orientation = newOrientation;
        this.c1 = c1;
        this.c2 = c2;
        this.alpha = alpha;
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
                switch (orientation) {
                    case ShadowPanel.HORIZONTAL:
                        g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), 0, c2));
                        break;
                    case VERTICAL:
                        g2.setPaint(new GradientPaint(0, 0, c1, 0, getHeight(), c2));
                        break;
                    case SLANT:
                    default:
                        g2.setPaint(new GradientPaint(0, 0, c1, getWidth(),
                                getHeight(), c2));
                        break;
                }
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

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

}
