package com.thingtek.view.shell.warningManage;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.data.service.DisDataService;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.socket.data.entity.DataSearchPara;
import com.thingtek.view.component.panel.Check2SPinner;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.DisDataTableModel;
import com.thingtek.view.shell.BasePanel;
import com.thingtek.view.shell.DataPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * 图标 时间选择 曲线框
 */
public class DisWarningPanel extends BasePanel implements DataPanel {

    private int maxlineshow = 5;//最大显示曲线数
    private int onepagecount;

    public void setOnepagecount(int onepagecount) {
        this.onepagecount = onepagecount;
    }

    public void setMaxlineshow(int maxlineshow) {
        this.maxlineshow = maxlineshow;
    }

    private int clttype = 4;
    @Resource
    private UnitService unitService;
    @Resource
    private DisDataService dataService;
    @Resource
    private DisDataTableModel tablemodel;
    private JTable table;
    private DataSearchPara para;

    private Check2SPinner c1;
    private Check2SPinner c2;
    private JSplitPane center;
    private JPanel left;
    private JPanel leftcenter;
    private CardLayout leftcard;
    private LinePanel linePanel;

    private boolean admin;

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public DisWarningPanel init() {
        this.setLayout(new BorderLayout());
        center = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
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

    private JButton head;
    private JButton tail;
    private JButton back;
    private JButton next;

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
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showwait_left();
                getSearchPara();
                refreashData();
            }
        });
        leftbottom.add(search);
        delete = new JButton("删除");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Map<DisUnitBean, List<Date>> datamap = getDateMap(datas);
                        dataService.deleteData(datamap);
                        refreashData();
                    }
                }).start();
                rightcard.show(right, "null");

            }
        });

        clear = new JButton("清空");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<DisUnitBean, List<Date>> datamap = getDateMap(datas);
                        dataService.deleteData(datamap);
                        refreashData();
                    }
                }).start();
                rightcard.show(right, "null");
            }
        });
        if (admin) {
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
        head = new JButton("首页");
        head.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                para.tofirst();
                showwait_left();
                refreashData();
            }
        });
        tablebottom.add(head);
        back = new JButton("上一页");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                para.toback();
                showwait_left();
                refreashData();
            }
        });
        tablebottom.add(back);
        next = new JButton("下一页");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                para.tonext();
                showwait_left();
                refreashData();
            }
        });
        tablebottom.add(next);
        tail = new JButton("尾页");
        tail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
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
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
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
                    linePanel.addDatas(lineDatas.toArray(new DisDataBean[0]));
                }
//                System.out.println(System.currentTimeMillis() - start);
                rightcard.show(right, "line");
                table.addMouseListener(tablemouseadaper);
            }
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

    private Map<DisUnitBean, List<Date>> getDateMap(Vector<DisDataBean> datas) {
        Map<DisUnitBean, List<Date>> datamap = new HashMap<>();
        for (DisDataBean data : datas) {
            DisUnitBean unit = (DisUnitBean) unitService.getUnitByNumber(clttype, data.getUnit_num());
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
        para.setClttype(clttype);
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
        /*if (t1 == null || t2 == null) {
            if (t1 != null) {
                c1.setTime(t1);
                c2.setTime(t1);
                c2.add(Calendar.DAY_OF_MONTH, 1);
            } else if (t2 != null) {
                c1.setTime(t2);
                c2.setTime(t2);
                c1.add(Calendar.DAY_OF_MONTH, -1);
            } else {
                c1.add(Calendar.DAY_OF_MONTH, -1);
            }
        } else {
            if (t1.after(t2)) {
                errorMessage("起始时间应位于结束时间之前");
                para = null;
                return;
            }
            c1.setTime(t1);
            c2.setTime(t2);
        }*/


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
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<DisDataBean> dataList = dataService.getDataInfo(para);
                DisDataBean[] a = dataList.toArray(new DisDataBean[0]);
                Arrays.sort(a, new Comparator<DisDataBean>() {
                    @Override
                    public int compare(DisDataBean o1, DisDataBean o2) {
                        return (int) (o2.getInserttime().getTime() - o1.getInserttime().getTime());
                    }
                });
                ListIterator<DisDataBean> i = dataList.listIterator();
                for (DisDataBean e : a) {
                    i.next();
                    i.set(e);
                }
                addData(dataList);
                leftcard.show(leftcenter, "table");
                setEnable(true);
            }
        }).start();
    }

    private void setEnable(boolean flag) {
        search.setEnabled(flag);
        delete.setEnabled(flag);
        clear.setEnabled(flag);
    }
}
