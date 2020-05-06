package com.thingtek.view.component.panel;

import org.jfree.chart.StandardChartTheme;

import javax.swing.*;
import java.awt.*;

public abstract class BaseGhaph extends JPanel {

    protected StandardChartTheme getTheme() {
        StandardChartTheme theme = (StandardChartTheme) StandardChartTheme.createJFreeTheme();
        theme.setLargeFont(new Font("微软雅黑", Font.PLAIN, 12));
        theme.setRegularFont(new Font("微软雅黑", Font.PLAIN, 12));
        theme.setSmallFont(new Font("微软雅黑", Font.PLAIN, 8));
        theme.setExtraLargeFont(new Font("微软雅黑", Font.PLAIN, 12));
        theme.setPlotBackgroundPaint(Color.WHITE);
        theme.setDomainGridlinePaint(Color.LIGHT_GRAY);
        theme.setRangeGridlinePaint(Color.LIGHT_GRAY);
        return theme;
    }


}
