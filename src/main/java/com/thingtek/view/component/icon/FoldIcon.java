package com.thingtek.view.component.icon;

import com.thingtek.util.Util;
import com.thingtek.view.component.factory.MyColorFactory;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class FoldIcon implements Icon, Serializable {
	/**
	 * Paints the horizontal bars for the
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		ButtonModel model = ((JButton) c).getModel();
		Graphics2D g2 = (Graphics2D) g.create();
		g2.translate(x, y);
		if (c.isEnabled()) {
			if (model.isRollover()) {
				if (model.isPressed()) {
					g2.setColor(new Color(0, 0, 0, 50));
					g2.fillRect(0, 0, getIconWidth(), getIconHeight());
				} else {
					g2.setColor(new Color(255, 255, 255, 100));
					g2.fillRect(0, 0, getIconWidth(), getIconHeight());
					g2.translate(0, -1);
				}
			}
			g2.setColor(Color.WHITE);
		} else {
			g2.setColor(MyColorFactory.InactiveControlTextColor);
		}

		g2.fillRect(6, 11, 12, 2);
		if (model.isSelected()) {
			g2.fillRect(11, 6, 2, 12);
		}
		g2.dispose();
	}

	/**
	 * Created a stub to satisfy the interface.
	 */
	public int getIconWidth() {
		return 24;
	}

	/**
	 * Created a stub to satisfy the interface.
	 */
	public int getIconHeight() {
		return 24;
	}

}
