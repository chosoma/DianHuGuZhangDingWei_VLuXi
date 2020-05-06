package com.thingtek.view.component.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;

public class MinIcon implements Icon, Serializable {
	/**
	 * Paints the horizontal bars for the
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (c.isEnabled()) {
			int width = getIconWidth();
			int height = getIconHeight();
			Graphics2D g2 = (Graphics2D) g.create();
			g2.translate(x, y);

			ButtonModel model = ((JButton) c).getModel();
			if (model.isRollover()) {
				if (model.isPressed()) {
					g2.setColor(new Color(0, 0, 0, 50));
					g2.fillRect(0, 0, width, height);
					g2.setColor(Color.LIGHT_GRAY);
				} else {
					g2.setColor(new Color(255, 255, 255, 100));
					g2.fillRect(0, 0, width, height);
					g2.setColor(Color.WHITE);
					g2.translate(0, -1);
				}
			} else {
				g2.setColor(new Color(226, 226, 226));
			}
			for (int line = 0; line < 3; line++) {
				g2.drawLine(6, 13 + line, 16, 13 + line);
			}
			// g2.drawLine(6, 13, 16, 12);
			// g2.drawLine(6, 14, 16, 13);
			// g2.drawLine(6, 15, 16, 14);
			g2.dispose();
		}
	}

	/**
	 * Created a stub to satisfy the interface.
	 */
	public int getIconWidth() {
		return 23;
	}

	/**
	 * Created a stub to satisfy the interface.
	 */
	public int getIconHeight() {
		return 20;
	}

}
