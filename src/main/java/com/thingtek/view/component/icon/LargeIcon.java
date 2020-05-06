package com.thingtek.view.component.icon;


import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class LargeIcon implements Icon, Serializable {

    private Image icon;

    public LargeIcon(Image icon) {
        this.icon = icon;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x, y);
        if (c.isEnabled()) {
            g2.drawImage(icon, 0, 0, 100, 100, c);
        }
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return 100;
    }

    @Override
    public int getIconHeight() {
        return 100;
    }

}
