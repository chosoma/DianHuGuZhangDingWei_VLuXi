package com.thingtek.view.component.button;



import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;

public class ScrollButton extends BasicArrowButton {

	private boolean isFreeStanding = false;

	private int buttonWidth;

	public ScrollButton(int direction, int width, boolean freeStanding) {
		super(direction);
		buttonWidth = width;
		isFreeStanding = freeStanding;
	}

	public void setFreeStanding(boolean freeStanding) {
		isFreeStanding = freeStanding;
	}

	public void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		int w = width;
		int h = height;
		int arrowHeight = (height + 1) / 3;
		boolean leftToRight = this.getComponentOrientation().isLeftToRight();
		boolean isEnabled = getParent().isEnabled();
		boolean isPressed = getModel().isPressed();
		boolean isRollover = getModel().isRollover();
		Color arrowColor;
		int off = 0;
		Graphics2D g2 = (Graphics2D) g.create();
		if (isEnabled) {
			if (isPressed) {
				arrowColor = new Color(77, 97, 133);
				off = 1;
			} else if (isRollover) {
				arrowColor = new Color(98, 164, 214);
			} else {
				arrowColor = new Color(187, 195, 201);
			}
		} else {
			arrowColor = new Color(153, 153, 153);
		}

		if (getDirection() == NORTH) {
			// Draw the arrow
			g2.setColor(arrowColor);
			int startY = ((h + 1) - arrowHeight) / 2 + off;
			int startX = (w / 2) + off;

			for (int line = 0; line < arrowHeight; line++) {
				g2.drawLine(startX - line, startY + line, startX + line, startY
						+ line);
			}

			if (!isEnabled) {
				drawDisabledBorder(g2, 0, 0, width, height + 1);
			}

			if (!isFreeStanding) {
				height -= 1;
				g2.translate(0, 1);
				width -= 2;
				if (!leftToRight) {
					g2.translate(1, 0);
				}
			}
		} else if (getDirection() == SOUTH) {

			// Draw the arrow
			g2.setColor(arrowColor);

			int startY = (((h + 1) - arrowHeight) / 2) + arrowHeight - 1 + off;
			int startX = (w / 2) + off;
			for (int line = 0; line < arrowHeight; line++) {
				g2.drawLine(startX - line, startY - line, startX + line, startY
						- line);
			}

			if (!isEnabled) {
				drawDisabledBorder(g2, 0, -1, width, height + 1);
			}

			if (!isFreeStanding) {
				height -= 1;
				width -= 2;
				if (!leftToRight) {
					g2.translate(1, 0);
				}
			}
		} else if (getDirection() == EAST) {

			// Draw the arrow
			g2.setColor(arrowColor);
			int startX = (((w + 1) - arrowHeight) / 2) + arrowHeight - 1 + off;
			int startY = (h / 2) + off;
			for (int line = 0; line < arrowHeight; line++) {
				g2.drawLine(startX - line, startY - line, startX - line, startY
						+ line);
			}

			if (!isEnabled) {
				drawDisabledBorder(g2, -1, 0, width + 1, height);
			}

			if (!isFreeStanding) {
				height -= 2;
				width -= 1;
			}
		} else if (getDirection() == WEST) {

			// Draw the arrow
			g2.setColor(arrowColor);
			int startX = (((w + 1) - arrowHeight) / 2) + off;
			int startY = (h / 2) + off;
			for (int line = 0; line < arrowHeight; line++) {
				g2.drawLine(startX + line, startY - line, startX + line, startY
						+ line);
			}

			if (!isEnabled) {
				drawDisabledBorder(g2, 0, 0, width + 1, height);
			}

			if (!isFreeStanding) {
				height -= 2;
				width -= 1;
				g2.translate(1, 0);
			}
		}
		g2.dispose();
	}

	public void drawDisabledBorder(Graphics g, int x, int y, int w, int h) {
		g.translate(x, y);
		g.setColor(new Color(153, 153, 153));
		g.drawRect(0, 0, w - 1, h - 1);
		g.translate(-x, -y);
	}

	public Dimension getPreferredSize() {
		return new Dimension(buttonWidth, buttonWidth);
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public Dimension getMaximumSize() {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	public int getButtonWidth() {
		return buttonWidth;
	}

	public boolean isFocusable() {
		return false;
	}

	// 设置按钮按下后无虚线框
	public boolean isFocusPainted() {
		return false;
	}

	// 取消绘制按钮内容区域
	public boolean isContentAreaFilled() {
		return false;
	}

	public boolean isBorderPainted() {
		return false;
	}
}
