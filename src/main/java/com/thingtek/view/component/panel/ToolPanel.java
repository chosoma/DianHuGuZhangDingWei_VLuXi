package com.thingtek.view.component.panel;

import com.thingtek.util.Util;

import javax.swing.*;
import java.awt.*;

public class ToolPanel extends JPanel {
    private Color c1, c2;
    private Color DefaultC1 = new Color(251, 251, 251), DefaultC2 = new Color(
            235, 235, 235);

    public ToolPanel() {
        super();
        init();
    }

    public ToolPanel(LayoutManager layout) {
        super(layout);
        init();
    }

    public ToolPanel(Color c1, Color c2) {
        super();
        this.c1 = c1;
        this.c2 = c2;
        init();
    }

    public ToolPanel(Color c1, Color c2, LayoutManager layout) {
        super(layout);
        this.c1 = c1;
        this.c2 = c2;
        init();
    }

    private void init() {
        this.setOpaque(false);// 很必要
        if (c1 == null) {
            c1 = DefaultC1;
        }
        if (c2 == null) {
            c2 = DefaultC2;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int h = getHeight(), w = getWidth();
        g2.setPaint(new GradientPaint(0, 0, c1, w, 0, c2));
        g2.fillRect(0, 0, w, h);
        g2.setColor(Color.WHITE);
        g2.setComposite(Util.AlphaComposite_50F);
        g2.drawLine(0, 0, 0, h);
        g2.drawLine(0, 0, w, 0);
        g2.setColor(Color.DARK_GRAY);
        g2.setComposite(Util.AlphaComposite_30F);
        g2.drawLine(w - 1, 0, w - 1, h);
        g2.drawLine(0, h - 1, w, h - 1);
        g2.dispose();
        super.paintComponent(g);
    }

}
