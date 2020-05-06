package com.thingtek.view.component.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;

public class MaxIcon implements Icon, Serializable {
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

			if (model.isSelected()) {
				g2.drawLine(9, 5, 16, 5);
				g2.drawLine(9, 6, 16, 6);
				g2.drawLine(16, 7, 16, 10);
				g2.drawLine(15, 11, 16, 11);

				g2.drawLine(6, 8, 13, 8);
				g2.drawRect(6, 9, 7, 6);
			} else {
				g2.drawLine(6, 5, 16, 5);
				g2.drawLine(6, 6, 16, 6);
				g2.drawRect(6, 7, 10, 8);
			}
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
