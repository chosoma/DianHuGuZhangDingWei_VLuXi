package com.thingtek.view.component.icon;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;

public class CheckBoxIcon implements Icon, UIResource {
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x, y);
        ButtonModel model = ((AbstractButton) c).getModel();

        int w = getIconWidth();
        int h = getIconHeight();
        if (model.isEnabled()) {
            // 边框
            g2.setColor(new Color(28, 81, 128));
            g2.drawRect(0, 0, w - 1, h - 1);
            // 背景
            if (model.isRollover()) {
                if (model.isPressed() && model.isArmed()) {
                    g2.setPaint(new GradientPaint(1, 1, new Color(176, 176, 167), w - 1, h - 1, new Color(241, 239, 223)));
                    g2.fillRect(1, 1, w - 2, h - 2);
                } else {
                    g2.setPaint(new GradientPaint(1, 1, new Color(255, 240, 207), w - 1, h - 1, new Color(248, 179, 48)));
                    g2.fillRect(1, 1, w - 2, h - 2);
                    g2.setColor(new Color(231, 231, 227));
                    g2.fillRect(3, 3, 7, 7);
                }
            } else {
                g2.setPaint(new GradientPaint(1, 1, new Color(220, 220, 215),
                        w - 1, h - 1, Color.WHITE));
                g2.fillRect(1, 1, w - 2, h - 2);
            }
            g2.setColor(new Color(33, 161, 33));
        } else {
            g2.setColor(new Color(202, 200, 187));
            g2.drawRect(0, 0, w - 1, h - 1);
        }

        if (model.isSelected()) {
            g2.drawLine(5, 7, 9, 3);
            g2.drawLine(5, 8, 9, 4);
            g2.drawLine(5, 9, 9, 5);
            g2.drawLine(3, 5, 3, 7);
            g2.drawLine(4, 6, 4, 8);
        }
        g2.dispose();
    }

    public int getIconWidth() {
        return 13;
    }

    public int getIconHeight() {
        return 13;
    }

}
