package com.thingtek.view.component.icon;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class CloseIcon implements Icon, Serializable {
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
					g2.setColor(new Color(118, 7, 10));
					g2.fillRect(0, 0, width, height);
					g2.setColor(Color.LIGHT_GRAY);
				} else {
					g2.setColor(new Color(156, 14, 20));
					g2.fillRect(0, 0, width, height);
					g2.setColor(Color.WHITE);
					g2.translate(0, -1);
				}
			} else {
				g2.setColor(new Color(226, 226, 226));
			}

			g2.drawLine(6, 6, 15, 15);
			g2.drawLine(7, 6, 15, 14);
			g2.drawLine(7, 5, 16, 14);

			g2.drawLine(15, 5, 6, 14);
			g2.drawLine(15, 6, 7, 14);
			g2.drawLine(16, 6, 7, 15);
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
