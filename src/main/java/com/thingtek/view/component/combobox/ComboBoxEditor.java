package com.thingtek.view.component.combobox;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;

public class ComboBoxEditor extends BasicComboBoxEditor {
	public ComboBoxEditor() {

		super();
		// editor.removeFocusListener(this);
		editor = new JTextField("", 9) {
			// workaround for 4530952
			public void setText(String s) {
				if (getText().equals(s)) {
					return;
				}
				super.setText(s);
			}

			// The preferred and minimum sizes are overriden and padded by
			// 4 to keep the size as it previously was. Refer to bugs
			// 4775789 and 4517214 for details.
			public Dimension getPreferredSize() {
				Dimension pref = super.getPreferredSize();
				pref.height += 4;
				return pref;
			}

			public Dimension getMinimumSize() {
				Dimension min = super.getMinimumSize();
				min.height += 4;
				return min;
			}
		};

		editor.setBorder(new EditorBorder());
	}


	protected static Insets editorBorderInsets = new Insets(2, 2, 2, 0);
	private static final Insets SAFE_EDITOR_BORDER_INSETS = new Insets(2, 2, 2,
			0);

	class EditorBorder extends AbstractBorder {
		public void paintBorder(Component c, Graphics g, int x, int y, int w,
				int h) {
			g.translate(x, y);

			g.setColor(Color.BLACK);
			g.drawRect(0, 0, w, h - 1);
			g.setColor(Color.WHITE);
			g.drawRect(1, 1, w - 2, h - 3);

			g.translate(-x, -y);
		}

		public Insets getBorderInsets(Component c) {
			if (System.getSecurityManager() != null) {
				return SAFE_EDITOR_BORDER_INSETS;
			} else {
				return editorBorderInsets;
			}
		}
	}

	public static class UIResource extends ComboBoxEditor implements
			javax.swing.plaf.UIResource {
	}

}
