package com.thingtek.view.component.icon;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class SetIcon implements Icon, Serializable {
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

			g2.drawLine(6, 5, 16, 5);
			g2.drawLine(6, 6, 16, 6);
			g2.drawLine(6, 7, 16, 7);

			g2.drawLine(6, 10, 16, 10);
			g2.drawLine(7, 11, 15, 11);
			g2.drawLine(8, 12, 14, 12);
			g2.drawLine(9, 13, 13, 13);
			g2.drawLine(10, 14, 12, 14);
			g2.drawLine(11, 15, 11, 15);
			
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
