package com.thingtek.view.component.factory;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Map;

public @Data
class MyFontFactory {

    private Map<String, Font> fontMap = new Hashtable<>();

    public Font getFont(String key) {
        Font font = fontMap.get(key);
        return font != null ? font : new Font("宋体", Font.PLAIN, 12);
    }


    public static final String Font_Name = "宋体";
    public static final int Font_Style = Font.PLAIN;
    public static final Font FONT_36 = new Font(Font_Name, Font_Style, 36);
    public static final Font FONT_20 = new Font(Font_Name, Font_Style, 20);
    public static final Font FONT_18 = new Font(Font_Name, Font_Style, 18);
    public static final Font FONT_16 = new Font(Font_Name, Font_Style, 16);
    public static final Font FONT_15 = new Font(Font_Name, Font_Style, 15);
    public static final Font FONT_14 = new Font(Font_Name, Font_Style, 14);
    public static final Font FONT_13 = new Font(Font_Name, Font_Style, 13);
    public static final Font FONT_12 = new Font(Font_Name, Font_Style, 12);
    public static final Font FONT_11 = new Font(Font_Name, Font_Style, 11);
    public static final Font FONT_10 = new Font(Font_Name, Font_Style, 10);
    public static final Font FONT_9 = new Font(Font_Name, Font_Style, 9);

    public static final Font TitleFont = new Font("微软雅黑", Font.BOLD, 12);
    public static final Font TitleFont30 = new Font("微软雅黑", Font.BOLD, 30);

}
