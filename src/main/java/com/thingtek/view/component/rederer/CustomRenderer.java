package com.thingtek.view.component.rederer;

import org.jfree.chart.ChartColor;

import java.awt.*;

public class CustomRenderer extends org.jfree.chart.renderer.category.BarRenderer {

    private Paint[] colors;

    public CustomRenderer() {
        colors = ChartColor.createDefaultPaintArray();
    }

    //每根柱子以初始化的颜色不断轮循
    public Paint getItemPaint(int i, int j) {
        return colors[j % colors.length];
    }
}
