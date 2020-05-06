package com.thingtek.view.component.button;


import com.thingtek.util.Util;
import com.thingtek.view.component.button.base.BaseButton;
import com.thingtek.view.component.factory.MyFontFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;

@EqualsAndHashCode(callSuper = true)
public @Data
class SkipButton extends BaseButton {

    private int type;

    public SkipButton(String text, Icon icon, int type) {
        super(text, icon);
        this.type = type;
//        this.addActionListener(Shell.getInstance());
        this.setBorder(BorderFactory.createBevelBorder(0));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 120);
    }

//    public Color getForeground() {
//        return Color.BLUE;
//    }
//
//    public Font getFont() {
//        return MyFontFactory.TitleFont30;
//    }

    public boolean isBorderPainted() {
        return true;
    }

}
