package com.thingtek.view.component.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.plaf.UIResource;

public class RadioButtonIcon implements Icon, UIResource {

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.translate(x, y);
		ButtonModel model = ((AbstractButton) c).getModel();
		int w = getIconWidth();
		int h = getIconHeight();
		if (model.isEnabled()) {
			g2.setColor(new Color(28, 81, 128));
			drawBorder(g2);
			if (model.isRollover()) {
				if (model.isPressed() && model.isArmed()) {
					g2.setPaint(new GradientPaint(1, 1,
							new Color(176, 176, 167), w - 1, h - 1, new Color(
									241, 239, 223)));
					drawBackground(g2);
					if (model.isSelected()) {
						g2.setPaint(new GradientPaint(4, 4, new Color(85, 213,
								81), 8, 8, new Color(19, 146, 16)));
						drawCenter(g2);
					}
				} else {
					g2.setPaint(new GradientPaint(1, 1,
							new Color(255, 240, 207), w - 1, h - 1, new Color(
									248, 179, 48)));
					drawBackground(g2);
					if (model.isSelected()) {
						g2.setPaint(new GradientPaint(4, 4, new Color(85, 213,
								81), 8, 8, new Color(19, 146, 16)));
						drawCenter(g2);
					} else {
						g2.setColor(new Color(231, 231, 227));
						drawCenter(g2);
					}
				}
			} else {
				g2.setPaint(new GradientPaint(1, 1, new Color(220, 220, 215),
						w - 1, h - 1, Color.WHITE));
				drawBackground(g2);
				if (model.isSelected()) {
					g2.setPaint(new GradientPaint(4, 4, new Color(85, 213, 81),
							8, 8, new Color(19, 146, 16)));
					drawCenter(g2);
				}
			}
		} else {
			g2.setColor(new Color(202, 200, 187));
			drawBorder(g2);
			if (model.isSelected()) {
				drawCenter(g2);
			}
		}

		g2.dispose();
	}

	private void drawBackground(Graphics2D g2) {
		g2.drawLine(4, 1, 8, 1);
		g2.drawLine(4, 11, 8, 11);
		g2.fillRect(2, 2, 9, 9);
		g2.drawLine(1, 4, 1, 8);
		g2.drawLine(11, 4, 11, 8);
	}

	private void drawBorder(Graphics2D g2) {
		g2.drawLine(4, 0, 8, 0);
		g2.drawLine(4, 12, 8, 12);

		g2.drawLine(0, 4, 0, 8);
		g2.drawLine(12, 4, 12, 8);

		g2.drawLine(2, 1, 3, 1);
		g2.drawLine(9, 1, 10, 1);
		g2.drawLine(2, 11, 3, 11);
		g2.drawLine(9, 11, 10, 11);

		g2.drawLine(1, 2, 1, 3);
		g2.drawLine(1, 9, 1, 10);
		g2.drawLine(11, 2, 11, 3);
		g2.drawLine(11, 9, 11, 10);

	}

	private void drawCenter(Graphics2D g2) {
		g2.drawLine(5, 4, 7, 4);
		g2.fillRect(4, 5, 5, 3);
		g2.drawLine(5, 8, 7, 8);
	}

	public int getIconWidth() {
		return 13;
	}

	public int getIconHeight() {
		return 13;
	}

}
