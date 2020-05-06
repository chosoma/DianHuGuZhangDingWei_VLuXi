package com.thingtek.view.shell.warningManage;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import com.thingtek.view.component.panel.BaseGhaph;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class LinePanel extends BaseGhaph {

    public LinePanel() {
        setLayout(new BorderLayout());
        init();
    }

    private SimpleDateFormat ymdhmsFormat = new SimpleDateFormat("YYYY年MM月dd日 HH:mm:ss");
    private SimpleDateFormat hmsFormat = new SimpleDateFormat("HH:mm:ss");
    private DecimalFormat numberFormat = new DecimalFormat("#0.00");
    private XYSeriesCollection dataset;
    //    private TimeSeriesCollection dataset2;
    private StandardXYItemRenderer renderer;

    private void init() {
        dataset = new XYSeriesCollection();
        chart = ChartFactory.createXYAreaChart(null, "", "", dataset);
        XYPlot xyPlot = chart.getXYPlot();
//        timeAxis = new DateAxis();
//        timeAxis.setDateFormatOverride(hmsFormat);
//        timeAxis.setLabel("");
//        dateAxis = new DateAxis();
//        dateAxis.setDateFormatOverride(ymdhmsFormat);
//        dateAxis.setLabel("");
        NumberAxis valueAxis = new NumberAxis();
        valueAxis.setLabel("");
        valueAxis.setNumberFormatOverride(numberFormat);
        valueAxis.setAutoRangeIncludesZero(true);
        valueAxis.setAutoRange(true);//设置曲线平滑
        xyPlot.setRangeAxis(valueAxis);
//        xyPlot.setBackgroundAlpha(0.7f);
//        xyPlot.setForegroundAlpha(0.3f);
        renderer = new StandardXYItemRenderer();
        xyPlot.setRenderer(renderer);
//        renderer.setBaseShapesVisible(true);
//        renderer.setBaseSeriesVisible(false);

        StandardChartTheme theme = getTheme();
        theme.apply(chart);
        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
//        initData();
    }

    private JFreeChart chart;
    private DateAxis timeAxis;
    private DateAxis dateAxis;

    public void clear() {
        dataset.removeAllSeries();
//        dataset2.removeAllSeries();
        System.gc();
    }

    public void addData(DisDataBean chartDatas) {
        clear();
        BaseUnitBean unit = chartDatas.getUnit();
        int[] dataline = chartDatas.getData();
        XYSeries series = new XYSeries(
                "位置：" + unit.getPoint().getPoint_name() +
                        "，相位:" + unit.getPhase() +
                        "，时间:" + chartDatas.getInserttime());

        for (int i = 0; i < dataline.length; i++) {
            series.add(i, dataline[i]);
        }
        dataset.addSeries(series);
    }

    public void addDataOther() {

    }

    public void addDatas(DisDataBean... chartDatas) {
        clear();
        for (DisDataBean chartData : chartDatas) {
            try {
                BaseUnitBean unit = chartData.getUnit();
                int[] dataline = chartData.getData();
                XYSeries series = new XYSeries(
                        "编号:" + unit.getUnit_num() +
                                ",位置:" + unit.getPoint().getPoint_name() +
                                ",相位:" + unit.getPhase() +
                                ",时间:" + chartData.getInserttime());
                for (int j = 0; j < dataline.length; j++) {
                    series.add(j, dataline[j]);
                }
                dataset.addSeries(series);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initData() {
        clear();
        Calendar calendar = Calendar.getInstance();
        XYSeries series = new XYSeries(calendar.getTime());
        for (int i = 0; i < 100; i++) {
            series.add(i, 100);
        }
        dataset.addSeries(series);
    }

    public void delete(Map<DisUnitBean, List<Date>> dates) {


    }

//    public void setLight(Map<DisUnitBean, java.util.List<Date>> dates) {
//        for (int i = 0; i < dataset.getSeriesCount(); i++) {
//            renderer.setSeriesStroke(i, new BasicStroke(2.0f));
//        }
//        if (chart.getXYPlot().getDomainAxis() == timeAxis) {
//            Collection<List<Date>> lists = dates.values();
//            for (List<Date> list : lists) {
//                for (Date date : list) {
//                    Calendar c = Calendar.getInstance();
//                    c.setTime(date);
//                    int index = dataset.getSeriesIndex(getCalendarYMd(c));
//                    renderer.setSeriesStroke(index, new BasicStroke(10f));
//                }
//            }
//        } else if (chart.getXYPlot().getDomainAxis() == dateAxis) {
//            Set<DisUnitBean> units = dates.keySet();
//            for (DisUnitBean unit : units) {
//                int index = dataset.getSeriesIndex(unit.getUnit_name());
//                renderer.setSeriesStroke(index, new BasicStroke(10f));
//            }
//        }
//    }

    private void setCalendar1970(Calendar c) {
        c.set(Calendar.YEAR, 1970);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DAY_OF_MONTH, 1);
    }

    private void setCalendar0h0m0s(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
    }

    private String getCalendarYMD(Calendar c) {
        return c.get(Calendar.YEAR) + "年"
                + (c.get(Calendar.MONTH) + 1) + "月"
                + c.get(Calendar.DAY_OF_MONTH) + "日";
    }


}
