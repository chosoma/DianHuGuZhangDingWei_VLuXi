package com.thingtek.view.component.icon;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;

public class ShowDebugIcon implements Icon, Serializable {

	/**
	 * Paints the horizontal bars for the
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		ButtonModel model = ((AbstractButton) c).getModel();
		Graphics2D g2 = (Graphics2D) g.create();
		g2.translate(x, y);
		if (c.isEnabled()) {
			if (model.isSelected()) {
				g2.drawImage(IconFactory.showDebug_16, 0, 0, 16, 16, c);
			} else {
				g2.drawImage(IconFactory.showDebug_muted_16, 0, 0, 16, 16, c);
			}
		}
		g2.dispose();
	}

	/**
	 * Created a stub to satisfy the interface.
	 */
	public int getIconWidth() {
		return 16;
	}

	/**
	 * Created a stub to satisfy the interface.
	 */
	public int getIconHeight() {
		return 16;
	}
}
