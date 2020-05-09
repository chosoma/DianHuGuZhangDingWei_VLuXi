package com.thingtek.view.component.button;

import com.thingtek.view.component.button.base.BaseButton;

import javax.swing.*;

/**
 * 主要功能按钮
 *
 * @author black
 *
 */
public class ShellTitleButton extends BaseButton {

	public ShellTitleButton(String text, Icon icon) {
		super(text, icon);
	}

	/*public Dimension getPreferredSize() {
		return new Dimension(100, 52);
	}*/

//	public Color getForeground() {
//		return Color.BLACK;
//	}
//
//	public Font getFont() {
//		return MyFontFactory.TitleFont;
//	}

	public boolean isBorderPainted() {
		return false;
	}
}
