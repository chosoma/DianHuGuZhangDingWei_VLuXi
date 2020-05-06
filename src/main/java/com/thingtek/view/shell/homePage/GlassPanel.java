package com.thingtek.view.shell.homePage;

import com.thingtek.view.shell.BasePanel;

import java.awt.*;

public class GlassPanel extends BasePanel {

    private float alpha = 0.35f;

    public GlassPanel() {
        this.init();
    }

    public GlassPanel(float alpha) {
        this.alpha = alpha;
    }

    public GlassPanel(LayoutManager layout, float alpha) {
        setLayout(layout);
        this.alpha = alpha;
    }

    public GlassPanel init() {
        this.setOpaque(false);
        return this;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        Insets insets = getInsets();
        int x = insets.left, y = insets.top, w = getWidth() - x - insets.right, h = getHeight() - y - insets.bottom;
        g2.setPaint(new GradientPaint(x, y, new Color(255, 255, 255, 150), w, h, new Color(255, 255, 255, 250)));
        // g2.setColor(Color.WHITE);
        g2.fillRect(x, y, w, h);
        g2.setColor(Color.WHITE);
        g2.drawLine(x, y, w, y);
        g2.dispose();
        super.paintComponent(g);
    }
}
