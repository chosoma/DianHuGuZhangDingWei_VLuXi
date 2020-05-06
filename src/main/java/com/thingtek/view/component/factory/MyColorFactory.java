package com.thingtek.view.component.factory;


import java.awt.Color;
import java.util.Hashtable;
import java.util.Map;

public class MyColorFactory {

    private Map<String, Color> colorMap = new Hashtable<>();
    private Map<String, Color[]> colorsMap = new Hashtable<>();

    public Map<String, Color[]> getColorsMap() {
        return colorsMap;
    }

    public void setColorsMap(Map<String, Color[]> colorsMap) {
        this.colorsMap = colorsMap;
    }

    public Color getColor(String key) {
        Color color = colorMap.get(key);
        return color != null ? color : new Color(0);
    }

    public Color[] getColors(String key) {
        Color[] color = colorsMap.get(key);
        return color != null ? color : new Color[]{new Color(0xffffff), new Color(0xffffff), new Color(0xffffff), new Color(0xffffff)};
    }

    public Map<String, Color> getColorMap() {
        return colorMap;
    }

    public void setColorMap(Map<String, Color> colorMap) {
        this.colorMap = colorMap;
    }

    // 容器面板颜色
    public static final Color CONTENT_COLOR = Color.WHITE;

    // 左面板颜色(159,181,210)(238,238,238)
    public static final Color[] LEFT_PANE_COLOR = new Color[]{
            new Color(225, 235, 250), new Color(159, 181, 210)};

    // 按钮选中时色数组
    public static final Color[] SelectColors = new Color[]{Color.WHITE,
            Color.WHITE, Color.WHITE, Color.WHITE};
    // 按钮按下时色数组
    public static final Color[] PressColors = new Color[]{
            new Color(230, 230, 230),
            new Color(210, 210, 210),
            new Color(210, 210, 210),
            new Color(230, 230, 230)};
    // 按钮悬浮色数组
    public static final Color[] HoverColors = new Color[]{
            Color.WHITE,
            new Color(240, 240, 240),
            new Color(220, 220, 220),
            Color.WHITE};
    // 按钮外边框颜色
    public static final Color BUTTON_OUTER_BORDER = new Color(200, 200, 200);
    // 按钮内边框颜色
    public static final Color BUTTON_INNER_BORDER = Color.WHITE;

    // 按钮边框颜色
    public static final Color Component_Border_Color = new Color(127, 157, 185);

    public static final Color[] LABEL_BACKGROUNDS = new Color[]{
            new Color(225, 235, 250),
            new Color(155, 185, 245)};
    public static final Color[] tableSel_B = new Color[]{
            new Color(255, 225, 60),
            new Color(255, 200, 30)
    };
    // 奇数行前景色、背景色
    public static final Color tableOdd_F = Color.DARK_GRAY;
    public static final Color tableOdd_B = Color.WHITE;
    // 偶数行前景色、背景色
    public static final Color tableEven_F = Color.DARK_GRAY;
    public static final Color tableEven_B = new Color(240, 248, 253);

    public static final Color InactiveControlTextColor = new Color(153, 153, 153);// 组件不可用时的颜色
    public static final Color ENABLE = Color.BLACK;// 组件可用时的颜色

    public static final Color Shadow = new Color(120, 120, 120);

    public static final Color XY_line = new Color(147, 60, 227);// 折线(紫色)

    public static final Color rainColor = new Color(51, 102, 204);// 雨量颜色

    public static final Color TemperatureColor = new Color(255, 150, 0);// 温度颜色(黄色)

    public static final Color Color_X33 = new Color(51, 51, 51);// 字体颜色

    public static final Color Color_XF8F8FF = new Color(248, 248, 255);

    public static final Color DisabledBackground = new Color(245, 244, 234);

    public static final Color DisabledForeground = new Color(153, 153, 153);

    public static final Color SelectionBackground = new Color(214, 222, 235);

    // ----------滚动条------------
    public static final Color ScrollBarShadow = new Color(245, 244, 239);


    public static final Color Button_Focus = new Color(255, 187, 0);


    public static final Color Table_Grid = new Color(211, 217, 230);
}
