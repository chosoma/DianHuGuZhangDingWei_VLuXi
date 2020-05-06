package com.thingtek.view.component.factory;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

@Component
public class MyBorderFactory {
    // 组件边框
    public static final Border Component_Border = BorderFactory.createLineBorder(
            MyColorFactory.Component_Border_Color, 1);

    @Resource
    private MyColorFactory colorFactory;

    public Border getLineBorder(String string) {
        return BorderFactory.createLineBorder(colorFactory.getColor(string), 1);
    }

    public Border getEmptyBorder() {
        return BorderFactory.createEmptyBorder();
    }
}
