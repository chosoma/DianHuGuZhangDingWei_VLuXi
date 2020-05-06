package com.thingtek.view.component.button;

import com.thingtek.util.Util;

import javax.swing.*;
import java.awt.*;

public class CollectTitleButton extends JButton {
    static Insets insets = new Insets(1, 1, 3, 3);

    public CollectTitleButton() {
        super();
    }

    public CollectTitleButton(String text) {
        super(text, null);
    }

    public CollectTitleButton(Icon icon) {
        super(null, icon);
    }

    public CollectTitleButton(String text, Icon icon) {
        super(text, icon);
    }

    public Insets getInsets() {
        return insets;
    }

    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();
        if (isEnabled()) {
            if (model.isSelected()) {
                g2.setColor(new Color(127, 157, 185));
                g2.drawLine(0, 0, width, 0);
                g2.drawLine(0, 0, 0, height);
                g2.drawLine(0, height - 1, width, height - 1);
                g2.setColor(Color.WHITE);
                g2.fillRect(1, 1, width, height - 2);
            } else {
                if (model.isRollover()) {
                    if (model.isPressed()) {
                        g2.setColor(new Color(221, 221, 221));
                        g2.fillRect(insets.left, insets.top, width
                                - insets.left - insets.right, height
                                - insets.top - insets.bottom);
                    } else {
                        g2.setPaint(new GradientPaint(insets.left, insets.top, new Color(249, 249, 249), insets.left, height - insets.bottom, new Color(211, 211, 211)));
                        g2.fillRect(insets.left, insets.top, width - insets.left - insets.right, height - insets.top - insets.bottom);
                    }
                } else {
                    g2.setComposite(Util.AlphaComposite_50F);
                    g2.setColor(new Color(242, 242, 242));
                    g2.fillRect(insets.left, insets.top, width - insets.left - insets.right, height - insets.top - insets.bottom);
                }
                Util.DrawBorder(g2, new Color(127, 157, 185), width,
                        height);
            }
        } else {
            g2.setColor(new Color(153, 153, 153));
            g2.drawRect(0, 0, width - 1, height - 1);
        }
        g2.dispose();
        super.paintComponent(g);
    }

    public boolean isFocusable() {
        return false;
    }

    // 设置按钮按下后无虚线框
    public boolean isFocusPainted() {
        return false;
    }

    // 取消绘制按钮内容区域
    public boolean isContentAreaFilled() {
        return false;
    }

//    文字位置

    public int getVerticalTextPosition() {
        return SwingConstants.BOTTOM;
    }

    public int getHorizontalTextPosition() {
        return SwingConstants.CENTER;
    }

    public Color getForeground() {
        return Color.BLACK;
    }

    public boolean isBorderPainted() {
        return false;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(80, 80);
    }
}
