package com.thingtek.view.component.panel;

import com.thingtek.view.component.rederer.CustomRenderer;
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;

import org.jfree.ui.RectangleInsets;

import java.awt.*;
import java.text.DecimalFormat;

public class CategoryPanel extends BaseGhaph {

    private String valueAxisLabel = "";

    public void setValueAxisLabel(String valueAxisLabel) {
        this.valueAxisLabel = valueAxisLabel;
    }

    public CategoryPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    private DefaultCategoryDataset dataset;

    public void init() {
        dataset = new DefaultCategoryDataset();

        JFreeChart chart = ChartFactory.createBarChart3D("", "", valueAxisLabel, dataset, PlotOrientation.VERTICAL, false, false, false);
        Font font = new Font("微软雅黑", Font.PLAIN, 12);
        CategoryPlot plot = chart.getCategoryPlot();
        // 设置横虚线可见
        plot.setRangeGridlinesVisible(true);
        // 虚线色彩
        plot.setRangeGridlinePaint(Color.gray);
        NumberAxis vn = (NumberAxis) plot.getRangeAxis();
        vn.setAutoRangeIncludesZero(true);
        vn.setNumberFormatOverride(new DecimalFormat("#0.00")); // 数据轴数据标签的显示格式

        // x轴设置
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelFont(font);// 轴标题
        domainAxis.setTickLabelFont(font);// 轴数值
        // Lable（Math.PI/3.0）度倾斜
        // domainAxis.setCategoryLabelPositions(CategoryLabelPositions
        // .createUpRotationLabelPositions(Math.PI / 3.0));
        domainAxis.setMaximumCategoryLabelWidthRatio(6.00f);// 横轴上的 Lable

        // 设置距离图片左端距离
        domainAxis.setLowerMargin(0.1);
        // 设置距离图片右端距离
        domainAxis.setUpperMargin(0.1);
        // 设置 columnKey 是否间隔显示
        // domainAxis.setSkipCategoryLabelsToFit(true);
        plot.setDomainAxis(domainAxis);
        // 设置柱图背景色（注意，系统取色的时候要使用16位的模式来查看颜色编码，这样比较准确）
        plot.setBackgroundPaint(new Color(255, 255, 204));

        // y轴设置
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelFont(font);
        rangeAxis.setTickLabelFont(font);
        // 设置最高的一个 Item 与图片顶端的距离
        rangeAxis.setUpperMargin(0.15);
        // 设置最低的一个 Item 与图片底端的距离
        rangeAxis.setLowerMargin(0.15);
        plot.setRangeAxis(rangeAxis);
        // 使用自定义的渲染器
        CustomRenderer renderer = new CustomRenderer();
        // 设置柱子宽度
        renderer.setMaximumBarWidth(0.05);
        // 设置柱子高度
        renderer.setMinimumBarLength(0.05);
        // 设置柱子边框颜色
        renderer.setBaseOutlinePaint(Color.BLACK);
        // 设置柱子边框可见
        renderer.setDrawBarOutline(true);
        // 设置每个地区所包含的平行柱的之间距离
        renderer.setItemMargin(0.5);
        chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        // 显示每个柱的数值，并修改该数值的字体属性
        renderer.setIncludeBaseInRange(true);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        plot.setRenderer(renderer);
        // 设置柱的透明度
        plot.setForegroundAlpha(1.0f);
        // 背景色 透明度
        plot.setBackgroundAlpha(0.5f);
        chart.setTextAntiAlias(true);
        chart.setAntiAlias(true);//设置抗锯齿
        chart.setPadding(new RectangleInsets(5, 5, 5, 5));
        chart.setNotify(true);
        StandardChartTheme theme = getTheme();
        theme.apply(chart);
        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }

    public void addData(Number number, String str) {
        dataset.addValue(number, "", str);
    }

    public void clearData() {
        dataset.clear();
    }

}
