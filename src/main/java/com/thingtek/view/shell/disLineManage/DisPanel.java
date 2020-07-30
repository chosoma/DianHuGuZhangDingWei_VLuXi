package com.thingtek.view.shell.disLineManage;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.data.service.DisDataService;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.beanServiceDao.unit.service.LXUnitService;
import com.thingtek.socket.data.entity.DataSearchPara;
import com.thingtek.view.component.panel.Check2SPinner;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.DisDataTableModel;
import com.thingtek.view.shell.base.BasePanel;
import com.thingtek.view.shell.base.DataPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * 图标 时间选择 曲线框
 */
public class DisPanel extends BasePanel implements DataPanel {

    private int maxlineshow = 5;//最大显示曲线数
    private int onepagecount;

    public void setOnepagecount(int onepagecount) {
        this.onepagecount = onepagecount;
    }

    public void setMaxlineshow(int maxlineshow) {
        this.maxlineshow = maxlineshow;
    }

    @Resource
    private LXUnitService unitService;
    @Resource
    private DisDataService dataService;
    @Resource
    private DisDataTableModel tablemodel;
    private JTable table;
    private DataSearchPara para;

    private Check2SPinner c1;
    private Check2SPinner c2;
    private JPanel left;
    private JPanel leftcenter;
    private CardLayout leftcard;
    private LinePanel linePanel;

    @Override
    public DisPanel init() {
        this.setLayout(new BorderLayout());
        JSplitPane center = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        center.setDividerLocation(400);//设置分隔条位置
//        center.setLayout(new BorderLayout());
//        center.add(linePanel, BorderLayout.CENTER);
        initleft();
        initright();
        initializeTable();
        center.setLeftComponent(left);
        center.setRightComponent(right);
        this.add(center, BorderLayout.CENTER);
        return this;
    }

    private JButton search;
    private JButton delete;
    private JButton clear;

    private MouseAdapter tablemouseadaper;

    private void initleft() {
        left = new JPanel(new BorderLayout());
        initLeftCenter();
        initleftBottom();
    }

    private void initleftBottom() {
        JPanel leftbottom = new JPanel();
        Calendar calendar = Calendar.getInstance();
        c2 = new Check2SPinner(false, calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        c1 = new Check2SPinner(false, calendar.getTime());
        leftbottom.add(c1);
        leftbottom.add(c2);
        search = new JButton("查询");
        search.addActionListener(e -> {
            showwait_left();
            getSearchPara();
            refreashData();
//            dataService.delete();
        });
        /*Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                search.doClick();
            }
        },20000,10000);
*/
        leftbottom.add(search);
        delete = new JButton("删除");
        delete.addActionListener(e -> {
            Vector<DisDataBean> datas = getSelectInfos();
            if (datas.size() <= 0) {
                errorMessage("请选择要删除的信息");
                return;
            }
            int option = JOptionPane.showConfirmDialog(null, "确定删除?", "删除", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            }
            showwait_left();
            new Thread(() -> {
                final Map<LXUnitBean, List<Date>> datamap = getDateMap(datas);
                dataService.deleteData(datamap);
                refreashData();
            }).start();
            rightcard.show(right, "null");

        });

        clear = new JButton("清空");
        clear.addActionListener(e -> {
            Vector<DisDataBean> datas = getAllInfos();
            if (datas.size() <= 0) {
                errorMessage("当前表中无数据");
                return;
            }
            int option = JOptionPane.showConfirmDialog(null, "确定清空?", "清空", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            }
            showwait_left();
            new Thread(() -> {
                Map<LXUnitBean, List<Date>> datamap = getDateMap(datas);
                dataService.deleteData(datamap);
                refreashData();
            }).start();
            rightcard.show(right, "null");
        });
        if (isAdmin()) {
            leftbottom.add(delete);
            leftbottom.add(clear);
        }
        left.add(leftbottom, BorderLayout.SOUTH);
    }

    private void initLeftCenter() {
        leftcard = new CardLayout();
        leftcenter = new JPanel(leftcard);
        JPanel nullpanel = new JPanel();
        leftcenter.add(nullpanel, "null");
        JPanel centertable = new JPanel(new BorderLayout());
        leftcenter.add(centertable, "table");
        JPanel waitpanel = new JPanel(new BorderLayout());
        waitpanel.add(new JLabel(factorys.getIconFactory().getIcon("progress")));
        leftcenter.add(waitpanel, "wait");
        left.add(leftcenter, BorderLayout.CENTER);
        table = new JTable(tablemodel);
        tablemouseadaper = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                showline();
            }
        };
        table.addMouseListener(tablemouseadaper);
        JScrollPane scrollPane = new JScrollPane(table);
        centertable.add(scrollPane, BorderLayout.CENTER);
        JPanel tablebottom = new JPanel();
        tablebottom.setOpaque(false);
        JButton head = new JButton("首页");
        head.addActionListener(e -> {
            para.tofirst();
            showwait_left();
            refreashData();
        });
        tablebottom.add(head);
        JButton back = new JButton("上一页");
        back.addActionListener(e -> {
            para.toback();
            showwait_left();
            refreashData();
        });
        tablebottom.add(back);
        JButton next = new JButton("下一页");
        next.addActionListener(e -> {
            para.tonext();
            showwait_left();
            refreashData();
        });
        tablebottom.add(next);
        JButton tail = new JButton("尾页");
        tail.addActionListener(e -> {

        });
//        tablebottom.add(tail);
        centertable.add(tablebottom, BorderLayout.SOUTH);
    }

    private void showwait_left() {
        setEnable(false);
        leftcard.show(leftcenter, "wait");
    }

    private Thread thread;

    private void showline() {
        if (thread != null) {
            thread.interrupt();
        }
        rightcard.show(right, "wait");
        table.removeMouseListener(tablemouseadaper);
        thread = new Thread(() -> {
           /* DisDataBean data = getSelectInfo();
            DisDataBean lineData = dataService.getData(data);
            linePanel.addData(lineData);*/
            List<DisDataBean> datas = getSelectInfos();
//                long start = System.currentTimeMillis();
            if (datas.size() <= maxlineshow) {
                List<DisDataBean> lineDatas = new ArrayList<>();
                for (DisDataBean d : datas) {
                    lineDatas.add(dataService.getData(d));
                }
                linePanel.addDatas(lineDatas.toArray(new DisDataBean[datas.size()]));
            }
//                System.out.println(System.currentTimeMillis() - start);
            rightcard.show(right, "line");
            table.addMouseListener(tablemouseadaper);
        });
        thread.start();
    }

    private void addData(List<DisDataBean> datas) {
        Vector<Vector<Object>> vectors = new Vector<>();
        for (DisDataBean data : datas) {
            vectors.add(data.getDataTotalCol());
        }
        tablemodel.addDatas(vectors);
    }

    private DisDataBean getSelectInfo() {
        DisDataBean data = new DisDataBean();
        data.resolveTotalInfoTable(table, table.getSelectedRow());
        return data;
    }

    private Vector<DisDataBean> getSelectInfos() {
        int[] rows = table.getSelectedRows();
        Vector<DisDataBean> datas = new Vector<>();
        for (int row : rows) {
            DisDataBean data = new DisDataBean();
            data.resolveTotalInfoTable(table, row);
            datas.add(data);
        }
        return datas;
    }

    private Vector<DisDataBean> getAllInfos() {
        Vector<DisDataBean> datas = new Vector<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            DisDataBean data = new DisDataBean();
            data.resolveTotalInfoTable(table, i);
            datas.add(data);
        }
        return datas;
    }

    private Map<LXUnitBean, List<Date>> getDateMap(Vector<DisDataBean> datas) {
        Map<LXUnitBean, List<Date>> datamap = new HashMap<>();
        for (DisDataBean data : datas) {
            LXUnitBean unit = unitService.getUnitByNumber( data.getUnit_num());
            if (unit != null && datamap.containsKey(unit)) {
                datamap.get(unit).add(data.getInserttime());
            } else {
                List<Date> dates = new ArrayList<>();
                dates.add(data.getInserttime());
                datamap.put(unit, dates);
            }
        }
        return datamap;
    }

    private int[] getTableSelect() {
        return null;
    }

    private JPanel right;
    private CardLayout rightcard;

    private void initright() {
        rightcard = new CardLayout();
        JPanel nullpanel = new JPanel();
        right = new JPanel(rightcard);
        right.add(nullpanel, "null");
        linePanel = new LinePanel();
        right.add(linePanel, "line");
        JPanel waitpanel = new JPanel(new BorderLayout());
        waitpanel.add(new JLabel(factorys.getIconFactory().getIcon("progress")));
        right.add(waitpanel, "wait");
    }

    private void getSearchPara() {
        this.para = new DataSearchPara();
        para.setOnepagecount(onepagecount);
        Date t1 = c1.getTime();
        Date t2 = c2.getTime();
        Calendar calendar1 = Calendar.getInstance();
        if (t1 != null) {
            calendar1.setTime(t1);
        } else {
            calendar1.add(Calendar.DAY_OF_MONTH, -1);
        }
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        para.setT1(calendar1.getTime());

        Calendar calendar2 = Calendar.getInstance();
        if (t2 != null) {
            calendar2.setTime(t2);
        }
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        para.setT2(calendar2.getTime());
    }

    @Resource
    private TCR tcr;

    private void initializeTable() {
        tcr.initializeTable(table, tablemodel);
    }

    @Override
    public void refreashData() {
        if (para == null) {
            return;
        }
        new Thread(() -> {
            List<DisDataBean> dataList = dataService.getDataInfo(para);
            DisDataBean[] a = dataList.toArray(new DisDataBean[0]);
            Arrays.sort(a, (o1, o2) -> (int) (o2.getInserttime().getTime() - o1.getInserttime().getTime()));
            ListIterator<DisDataBean> i = dataList.listIterator();
            for (DisDataBean e : a) {
                i.next();
                i.set(e);
            }
            addData(dataList);
            leftcard.show(leftcenter, "table");
            setEnable(true);
        }).start();
    }

    private void setEnable(boolean flag) {
        search.setEnabled(flag);
        delete.setEnabled(flag);
        clear.setEnabled(flag);
    }
}
