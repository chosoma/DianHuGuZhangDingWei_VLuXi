package com.thingtek.view.component.border;

import com.thingtek.util.Util;
import com.thingtek.view.component.factory.MyColorFactory;

import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import java.awt.*;

public class ButtonBorder extends AbstractBorder implements
        UIResource {
    private static final long serialVersionUID = 5555555555555555555L;
    private static Insets borderInsets = new Insets(2, 2, 3, 3);

    public void paintBorder(Component c, Graphics g, int x, int y,
                            int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x, y);
        if (c.isEnabled()) {
            g2.setColor(new Color(32, 100, 202));
        } else {
            g2.setColor(MyColorFactory.InactiveControlTextColor);
        }
        g2.drawRect(1, 1, width - 4, height - 4);
        g2.setColor(MyColorFactory.Shadow);

        g2.setComposite(Util.AlphaComposite_100.derive(0.5f));// dark
        g2.drawLine(3, height - 2, width - 3, height - 2);// HORIZONTAL
        g2.drawLine(width - 2, 3, width - 2, height - 3);// VERTICAL
        g2.setComposite(Util.AlphaComposite_100.derive(0.3f));// light
        g2.drawLine(3, height - 1, width - 3, height - 1);// HORIZONTAL
        g2.drawLine(width - 1, 3, width - 1, height - 3);// VERTICAL
        // conner-dark
        g2.drawLine(width - 2, 2, width - 2, 2);// rightTop
        g2.drawLine(width - 2, height - 2, width - 2, height - 2);// rightBottom
        g2.drawLine(2, height - 2, 1, height - 2);// leftBottom
        // conner-light
        g2.setComposite(Util.AlphaComposite_100.derive(0.2f));
        g2.drawLine(width - 2, 1, width - 1, 2);// rightTop
        g2.drawLine(width - 1, height - 2, width - 2, height - 1);// rightBottom
        g2.drawLine(2, height - 1, 1, height - 2);// leftBottom
        // conner
        g2.setComposite(Util.AlphaComposite_100.derive(0.08f));
        g2.drawLine(width - 1, 1, width - 1, 1);// rightTop
        g2.drawLine(width - 1, height - 1, width - 1, height - 1);// rightBottom
        g2.drawLine(1, height - 1, 1, height - 1);// leftBottom
        g2.dispose();
    }

    public Insets getBorderInsets(Component c) {
        return borderInsets;
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        return borderInsets;
    }

}
