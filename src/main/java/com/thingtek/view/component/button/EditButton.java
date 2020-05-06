package com.thingtek.view.component.button;

import com.thingtek.util.Util;

import javax.swing.*;
import java.awt.*;

public class EditButton extends JButton {

	private Insets insets = new Insets(1, 1, 1, 1);

	public EditButton() {
		super();
	}

	public EditButton(String text) {
		super(text, null);
	}

	public EditButton(Icon icon) {
		super(null, icon);
	}

	public EditButton(String text, Icon icon) {
		super(text, icon);
	}

	public Insets getInsets() {
		return insets;
	}

	protected void paintComponent(Graphics g) {
		if (isEnabled()) {
			if (model.isRollover()) {
				int width = getWidth();
				int height = getHeight();
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setComposite(Util.AlphaComposite_100);
				if (model.isPressed()) {
					g2.setColor(new Color(221,221,221));
					g2.fillRect(insets.left, insets.top, width - insets.left
							- insets.right, height - insets.top - insets.bottom);
				} else {
					g2.setColor(Color.WHITE);
					g2.drawLine(insets.left, insets.top, width - insets.right
							- 1, insets.top);
					g2.drawLine(insets.left, insets.top + 1, insets.left,
							height - insets.bottom - 1);
					g2.setPaint(new GradientPaint(insets.left, insets.top + 1,
							new Color(249, 249, 249), insets.left, height
							- insets.bottom - 1, new Color(211, 211,
							211)));
					g2.fillRect(insets.left + 1, insets.top + 1, width
							- insets.left - insets.right - 1, height
							- insets.top - insets.bottom - 1);
				}
				g2.setColor(new Color(106, 106, 106));
				g2.drawRect(0, 0, width-1, height-1);
				g2.dispose();
			}
		}
		super.paintComponent(g);
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

	// 文字位置
	// public int getVerticalTextPosition() {
	// return SwingConstants.BOTTOM;
	// }
	//
	// public int getHorizontalTextPosition() {
	// return SwingConstants.CENTER;
	// }

	// public Color getForeground() {
	// return Color.WHITE;
	// }

	public boolean isBorderPainted() {
		return false;
	}
}
