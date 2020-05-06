package com.thingtek.view.component.button;



import com.thingtek.util.Util;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class ClickButton extends JButton {

    private Insets insets = new Insets(1, 1, 1, 1);

    public ClickButton() {
        super();
    }

    public ClickButton(String text) {
        super(text, null);
    }

    public ClickButton(Icon icon) {
        super(null, icon);
    }

    public ClickButton(String text, Icon icon) {
        super(text, icon);
    }

    public ClickButton(Action action) {
        super(action);
    }

    public Insets getInsets() {
        return insets;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();
        if (isEnabled()) {
//            Util.DrawBorder(g2, new Color(22, 106, 90), width, height); // 32.100.202
            if (model.isRollover()) {
                if (model.isPressed()) {
                    g2.setColor(new Color(27, 134, 114));//66.113.201
                } else {
                    g2.setPaint(new GradientPaint(insets.left, insets.top,
                            new Color(30, 161, 139), insets.left, height//127.175.247
                            - insets.bottom, new Color(27, 134, 114)));//66.113.201
                }
                g2.fillRect(insets.left, insets.top, width - insets.left
                        - insets.right, height - insets.top - insets.bottom);
            } else {
                g2.setColor(new Color(32, 169, 147));//134.172.228
                g2.drawLine(insets.left, insets.top, width - insets.left
                        - insets.right, insets.top);
                g2.setPaint(new GradientPaint(insets.left, insets.top + 1,
                        new Color(30, 161, 139), insets.left, height//98.147.221
                        - insets.bottom, new Color(27, 134, 114)));//66.113.201
                g2.fillRect(insets.left, insets.top + 1, width - insets.left
                        - insets.right, height - insets.top - insets.bottom - 1);
            }
        } else {
            g2.setColor(new Color(242, 242, 242));
            g2.fillRect(insets.left, insets.top, width - insets.left
                    - insets.right, height - insets.top - insets.bottom);
            g2.setColor(new Color(153, 153, 153));
            g2.drawRect(0, 0, width-1, height-1);
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

    // 文字位置
    // public int getVerticalTextPosition() {
    // return SwingConstants.BOTTOM;
    // }
    //
    // public int getHorizontalTextPosition() {
    // return SwingConstants.CENTER;
    // }

    public Color getForeground() {
        return Color.WHITE;
    }

    public boolean isBorderPainted() {
        return false;
    }
}
