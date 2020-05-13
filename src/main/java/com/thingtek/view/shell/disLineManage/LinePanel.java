package com.thingtek.view.shell.disLineManage;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.view.component.panel.BaseGhaph;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class LinePanel extends BaseGhaph {

    public LinePanel() {
        setLayout(new BorderLayout());
        init();
    }

    private SimpleDateFormat ymdhmsFormat = new SimpleDateFormat("YYYY年MM月dd日 HH:mm:ss");
    private DecimalFormat numberFormat = new DecimalFormat("#0.00");
    private XYSeriesCollection dataset;

    private void init() {
        dataset = new XYSeriesCollection();
        JFreeChart chart = ChartFactory.createXYAreaChart(null, "", "", dataset);
        XYPlot xyPlot = chart.getXYPlot();
        NumberAxis valueAxis = new NumberAxis();
        valueAxis.setLabel("");
        valueAxis.setNumberFormatOverride(numberFormat);
        valueAxis.setAutoRangeIncludesZero(true);
        valueAxis.setAutoRange(true);//设置曲线平滑
        xyPlot.setRangeAxis(valueAxis);
        StandardXYItemRenderer renderer = new StandardXYItemRenderer();
        xyPlot.setRenderer(renderer);

        StandardChartTheme theme = getTheme();
        theme.apply(chart);
        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }

    public void clear() {
        dataset.removeAllSeries();
        System.gc();
    }

    public void addDatas(DisDataBean... chartDatas) {
        clear();
        for (DisDataBean chartData : chartDatas) {
            try {
                LXUnitBean unit = chartData.getUnit();
                int[] dataline = chartData.getData();
                XYSeries series = new XYSeries(
                        "编号:" + unit.getUnit_num() +
                                ",管体:" + unit.getPipe().getPipe_name() +
                                ",段:" + unit.getPipe_page() +
                                ",时间:" + ymdhmsFormat.format(chartData.getInserttime()));
                for (int j = 0; j < dataline.length; j++) {
                    series.add(j, dataline[j]);
                }
                dataset.addSeries(series);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
