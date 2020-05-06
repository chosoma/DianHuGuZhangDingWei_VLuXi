package com.thingtek.view.component.panel;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.TextAnchor;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class LinePanelD extends BaseGhaph {

    public LinePanelD() {
        setLayout(new BorderLayout());
        init();
    }

    private SimpleDateFormat ymdhmsFormat = new SimpleDateFormat("YYYY年MM月dd日 HH:mm:ss");
    private SimpleDateFormat hmsFormat = new SimpleDateFormat("HH:mm:ss");
    private DecimalFormat numberFormat = new DecimalFormat("#0.00");
    private TimeSeriesCollection dataset;
    //    private TimeSeriesCollection dataset2;
    private XYLineAndShapeRenderer renderer;

    private void init() {
        dataset = new TimeSeriesCollection();
        chart = ChartFactory.createTimeSeriesChart(null, "", "水位(米)", dataset, true, true, true);
        XYPlot xyPlot = chart.getXYPlot();
        timeAxis = new DateAxis();
        timeAxis.setDateFormatOverride(hmsFormat);
        timeAxis.setLabel("时间(时:分:秒)");
        dateAxis = new DateAxis();
        dateAxis.setDateFormatOverride(ymdhmsFormat);
        dateAxis.setLabel("日期(-年-月-日 时:分:秒)");
        NumberAxis valueAxis = new NumberAxis();
        valueAxis.setLabel("水位(米)");
        valueAxis.setNumberFormatOverride(numberFormat);
        valueAxis.setAutoRangeIncludesZero(false);
        xyPlot.setRangeAxis(valueAxis);
        renderer = new XYLineAndShapeRenderer(true, true);
//        renderer.setBaseItemLabelsVisible(true);
//        renderer.setBaseShapesVisible(true);
//        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.INSIDE10, TextAnchor.BASELINE_LEFT));
//        renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
//        xyPlot.setBackgroundAlpha(0.7f);
//        xyPlot.setForegroundAlpha(0.3f);
        xyPlot.setRenderer(renderer);
        StandardChartTheme theme = getTheme();
        theme.apply(chart);
        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }

    private JFreeChart chart;
    private DateAxis timeAxis;
    private DateAxis dateAxis;

    private void init2() {
        dataset = new TimeSeriesCollection();
        chart = ChartFactory.createXYAreaChart(null, "x", "y", dataset);
        XYPlot xyPlot = chart.getXYPlot();
        timeAxis = new DateAxis();
        timeAxis.setDateFormatOverride(hmsFormat);
        dateAxis = new DateAxis();
        dateAxis.setDateFormatOverride(ymdhmsFormat);
        NumberAxis valueAxis = new NumberAxis();
        valueAxis.setNumberFormatOverride(numberFormat);
        valueAxis.setAutoRangeIncludesZero(false);
        xyPlot.setRangeAxis(valueAxis);
//        renderer = xyPlot.getRenderer();
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.INSIDE10, TextAnchor.BASELINE_LEFT));
        renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
        /*XYURLGenerator urlGenerator = new StandardXYURLGenerator();
        renderer.setURLGenerator(urlGenerator);*/
        StandardChartTheme theme = getTheme();
        theme.apply(chart);
        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }


    public void clear() {
        dataset.removeAllSeries();
//        dataset2.removeAllSeries();
        System.gc();
    }

    public void addData(Map<DisUnitBean, List<DisDataBean>> chartDatas) {
        clear();
        double maxValue = 0;
        if (chartDatas.size() <= 1) {
            chart.getXYPlot().setDomainAxis(timeAxis);
            XYToolTipGenerator toolTipGenerator = new StandardXYToolTipGenerator(
                    StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                    hmsFormat,
                    numberFormat);
            renderer.setBaseToolTipGenerator(toolTipGenerator);
            Calendar calendar1970 = Calendar.getInstance();
            setCalendar1970(calendar1970);
            setCalendar0h0m0s(calendar1970);
            timeAxis.setLowerBound(calendar1970.getTimeInMillis());
            calendar1970.add(Calendar.DAY_OF_MONTH, 1);
            timeAxis.setUpperBound(calendar1970.getTimeInMillis());
            TreeSet<DisDataBean> datas = new TreeSet<DisDataBean>(new Comparator<DisDataBean>() {
                @Override
                public int compare(DisDataBean o1, DisDataBean o2) {
                    if (o1.getInserttime().before(o2.getInserttime())) {
                        return -1;
                    } else if (o1.getInserttime().after(o2.getInserttime())) {
                        return 1;
                    }
                    return 0;
                }
            });
            Set<Map.Entry<DisUnitBean, List<DisDataBean>>> entrySet = chartDatas.entrySet();
            for (Map.Entry<DisUnitBean, List<DisDataBean>> entry : entrySet) {
                datas.addAll(entry.getValue());
            }
//            System.out.println("datas.size:" + datas.size());
            if (datas.size() <= 0) {
                return;
            }
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(datas.first().getInserttime());
            setCalendar0h0m0s(calendarStart);
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(datas.last().getInserttime());
            setCalendar0h0m0s(calendarEnd);
            calendarEnd.add(Calendar.DAY_OF_MONTH, 1);
            do {
                TimeSeries timeSeries = new TimeSeries(getCalendarYMd(calendarStart));
                Calendar calendarflag = Calendar.getInstance();
                calendarflag.setTime(calendarStart.getTime());
                calendarStart.add(Calendar.DAY_OF_MONTH, 1);
                int count = 0;
                for (DisDataBean data : datas) {
                    if (data.getInserttime().before(calendarStart.getTime())
                            && data.getInserttime().after(calendarflag.getTime())) {
                        count++;
                        Calendar c = Calendar.getInstance();
                        c.setTime(data.getInserttime());
                        setCalendar1970(c);
                        timeSeries.add(new Second(c.getTime()), 1);
                    }
                }
                if (timeSeries.getMaxY() > maxValue) {
                    maxValue = timeSeries.getMaxY();
                }
                if (count != 0)
                    dataset.addSeries(timeSeries);
            } while (calendarEnd.after(calendarStart));

        } else {
            chart.getXYPlot().setDomainAxis(dateAxis);
            XYToolTipGenerator toolTipGenerator = new StandardXYToolTipGenerator(
                    StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                    ymdhmsFormat,
                    numberFormat);
            renderer.setBaseToolTipGenerator(toolTipGenerator);
            Set<Map.Entry<DisUnitBean, List<DisDataBean>>> entrySet = chartDatas.entrySet();
            for (Map.Entry<DisUnitBean, List<DisDataBean>> entry : entrySet) {
                TimeSeries timeSeries = new TimeSeries(entry.getKey().getUnit_num());
                List<DisDataBean> datalist = entry.getValue();
                for (DisDataBean data : datalist) {
                    timeSeries.add(new Second(data.getInserttime()), 1);
                }
                if (timeSeries.getMaxY() > maxValue) {
                    maxValue = timeSeries.getMaxY();
                }
                dataset.addSeries(timeSeries);
            }
        }
        chart.getXYPlot().getRangeAxis().setUpperBound(maxValue * 1.2);
        for (int i = 0; i < dataset.getSeries().size(); i++) {
            renderer.setSeriesStroke(i, new BasicStroke(2.0f));
        }
        repaint();
    }

    public void addData2(Map<DisUnitBean, List<DisDataBean>> chartDatas) {
        clear();
        Set<Map.Entry<DisUnitBean, List<DisDataBean>>> entrySet = chartDatas.entrySet();
        for (Map.Entry<DisUnitBean, List<DisDataBean>> entry : entrySet) {
            TimeSeries timeSeries = new TimeSeries(entry.getKey().getUnit_num());
            List<DisDataBean> datalist = entry.getValue();
            for (DisDataBean data : datalist) {
                timeSeries.add(new Second(data.getInserttime()), 1);
            }
            /*Iterator<BaseBean> iterator = datalist.iterator();
            while (iterator.hasNext()){
                BaseBean data = iterator.next();
                iterator.remove();
                timeSeries.add(new Second(data.getInserttime()), data.getValue1());
            }*/
            dataset.addSeries(timeSeries);
//            dataset2.addSeries(timeSeries);
        }
        for (int i = 0; i < chartDatas.size(); i++) {
            renderer.setSeriesStroke(i, new BasicStroke());
        }
        repaint();
    }

    public void delete(Map<DisUnitBean, List<Date>> dates) {
        Set<Map.Entry<DisUnitBean, List<Date>>> entrySet = dates.entrySet();
        if (dates.size() <= 1) {
            for (Map.Entry<DisUnitBean, List<Date>> entry : entrySet) {
                for (Date date : entry.getValue()) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    TimeSeries timeSeries = dataset.getSeries(getCalendarYMd(c));
                    setCalendar1970(c);
                    timeSeries.delete(new Second(c.getTime()));
                }
            }
        } else {
            for (Map.Entry<DisUnitBean, List<Date>> entry : entrySet) {
                TimeSeries timeSeries = dataset.getSeries(entry.getKey().getUnit_num());
                for (Date date : entry.getValue()) {
                    timeSeries.delete(new Second(date));
                }
            }
        }
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

    private String getCalendarYMd(Calendar c) {
        return c.get(Calendar.YEAR) + "年"
                + (c.get(Calendar.MONTH) + 1) + "月"
                + c.get(Calendar.DAY_OF_MONTH) + "日";
    }


}
