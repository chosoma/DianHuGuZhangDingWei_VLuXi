package com.thingtek.view.component.combobox;

import javax.swing.*;
import java.awt.*;

public class ComboBoxButton extends JButton {

	protected JComboBox comboBox;
	protected JList listBox;
	protected CellRendererPane rendererPane;
	protected Icon comboIcon;
	protected boolean iconOnly = false;

	public final JComboBox getComboBox() {
		return comboBox;
	}

	public final void setComboBox(JComboBox cb) {
		comboBox = cb;
	}

	public final Icon getComboIcon() {
		return comboIcon;
	}

	public final void setComboIcon(Icon i) {
		comboIcon = i;
	}

	public final boolean isIconOnly() {
		return iconOnly;
	}

	public final void setIconOnly(boolean isIconOnly) {
		iconOnly = isIconOnly;
	}

	ComboBoxButton() {
		super("");
		DefaultButtonModel model = new DefaultButtonModel() {
			public void setArmed(boolean armed) {
				super.setArmed(isPressed() ? true : armed);
			}
		};
		setModel(model);
	}

	public ComboBoxButton(JComboBox cb, Icon i, CellRendererPane pane,
                          JList list) {
		this();
		comboBox = cb;
		comboIcon = i;
		rendererPane = pane;
		listBox = list;
		setEnabled(comboBox.isEnabled());
	}

	public ComboBoxButton(JComboBox cb, Icon i, boolean onlyIcon,
                          CellRendererPane pane, JList list) {
		this(cb, i, pane, list);
		iconOnly = onlyIcon;
	}


	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		// Set the background and foreground to the combobox colors.
		if (enabled) {
			setBackground(comboBox.getBackground());
			setForeground(comboBox.getForeground());
		} else {
			setBackground(UIManager.getColor("ComboBox.disabledBackground"));
			setForeground(UIManager.getColor("ComboBox.disabledForeground"));
		}
	}

	public void paint(Graphics g) {
		if (comboBox.isEnabled()) {
			g.setColor(ComboBoxUtil.Border_Color);
		} else {
			g.setColor(ComboBoxUtil.DisabledForeground);
		}
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		g.setColor(ComboBoxUtil.Background);
		g.fillRect(0, 1, getWidth() - 1, getHeight() - 2);

		boolean leftToRight = comboBox.getComponentOrientation()
				.isLeftToRight();
		boolean isEnabled = getParent().isEnabled();
		boolean isPressed = getModel().isPressed();
		boolean isRollover = getModel().isRollover();

		Insets insets = getInsets();

		int width = getWidth() - (insets.left + insets.right);
		int height = getHeight() - (insets.top + insets.bottom);

		if (height <= 0 || width <= 0) {
			return;
		}

		int left = insets.left;
		int top = insets.top;
		int right = left + (width - 1);
		int bottom = top + (height - 1);

		int iconWidth = 0;
		int iconLeft = (leftToRight) ? right : left;

		// Paint the icon
		if (comboIcon != null) {
			iconWidth = comboIcon.getIconWidth();
			int iconHeight = comboIcon.getIconHeight();
			int iconTop = 0;

			if (iconOnly) {
				iconLeft = (getWidth() / 2) - (iconWidth / 2);
				iconTop = (getHeight() / 2) - (iconHeight / 2);
			} else {
				if (leftToRight) {
					iconLeft = (left + (width - 1)) - iconWidth;
				} else {
					iconLeft = left;
				}
				iconTop = (top + ((bottom - top) / 2)) - (iconHeight / 2);
			}

			if (isEnabled) {
				if (isPressed) {
					g.setColor(ComboBoxUtil.Button_Pressed);
					iconLeft++;
					iconTop++;
				} else if (isRollover) {
					g.setColor(ComboBoxUtil.Button_Rollover);
				} else {
					g.setColor(ComboBoxUtil.Button_Default);
				}
			} else {
				g.setColor(ComboBoxUtil.DisabledForeground);
			}
			comboIcon.paintIcon(this, g, iconLeft, iconTop);
		}
	}

	public Dimension getMinimumSize() {
		Dimension ret = new Dimension();
		Insets insets = getInsets();
		ret.width = insets.left + getComboIcon().getIconWidth() + insets.right;
		ret.height = insets.bottom + getComboIcon().getIconHeight()
				+ insets.top;
		return ret;
	}

}
