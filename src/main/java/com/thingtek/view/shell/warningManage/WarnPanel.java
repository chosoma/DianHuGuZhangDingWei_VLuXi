package com.thingtek.view.shell.warningManage;

import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.socket.data.entity.DataSearchPara;
import com.thingtek.view.component.panel.Check2SPinner;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.WarnTableModel;
import com.thingtek.view.shell.BasePanel;
import com.thingtek.view.shell.DataPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * 增加查询分页
 */
public class WarnPanel extends BasePanel implements DataPanel {

    private boolean admin;

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public WarnPanel init() {
        setLayout(new BorderLayout());
        initCenter();
        initBottom();
        return this;
    }

    private void initBottom() {
        JPanel bottom = new JPanel();

        bottom.add(new JLabel("类型:"));

        this.add(bottom, BorderLayout.NORTH);

        Calendar calendar = Calendar.getInstance();
        c2 = new Check2SPinner(false, calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        c1 = new Check2SPinner(false, calendar.getTime());
        bottom.add(c1);
        bottom.add(c2);
        search = new JButton("查询");
        search.addActionListener(e -> {
            getSearchPara();
            refreashData();
        });
        bottom.add(search);

        delete = new JButton("删除");
        delete.addActionListener(e -> {
            Vector<WarnBean> datas = getSelectInfos();
            if (datas.size() <= 0) {
                errorMessage("请选择要删除的信息");
                return;
            }
            Map<WarnBean, List<Date>> datamap = getDateMap(datas);
            if (warnService.deleteWarn(datamap)) {
                successMessage("删除成功");
                search.doClick();
            } else {
                falseMessage("删除失败");
            }

        });

        clear = new JButton("清空");
        clear.addActionListener(e -> {
            Vector<WarnBean> datas = getAllInfos();
            if (datas.size() <= 0) {
                errorMessage("当前表中无信息");
                return;
            }
            Map<WarnBean, List<Date>> datamap = getDateMap(datas);
            if (warnService.deleteWarn(datamap)) {
                successMessage("清空成功");
                search.doClick();
            } else {
                falseMessage("清空失败");
            }
        });
        if (admin) {
            bottom.add(delete);
            bottom.add(clear);
        }
        this.add(bottom, BorderLayout.SOUTH);

    }

    private Check2SPinner c1;

    private Check2SPinner c2;
    private JButton search;
    private JButton delete;
    private JButton clear;
    private CardLayout card;

    private void initCenter() {
        card = new CardLayout();
        center = new JPanel(card);
        table = new JTable(warnTableModel);
        JScrollPane jScrollPane = new JScrollPane(table);
        center.add(jScrollPane, "table");
        initializeTable();
        JPanel waitpanel = new JPanel(new BorderLayout());
        waitpanel.add(new JLabel(factorys.getIconFactory().getIcon("progress")));
        center.add(waitpanel, "wait");
        this.add(center, BorderLayout.CENTER);
    }



    private Map<WarnBean, List<Date>> getDateMap(Vector<WarnBean> datas) {
        Map<WarnBean, List<Date>> datamap = new HashMap<>();
        for (WarnBean data : datas) {
            if (datamap.containsKey(data)) {
                datamap.get(data).add(data.getInserttime());
            } else {
                List<Date> dates = new ArrayList<>();
                dates.add(data.getInserttime());
                datamap.put(data, dates);
            }
        }
        return datamap;
    }

    private void addData(List<WarnBean> datas) {
        List<Vector<Object>> vectors = new ArrayList<>();
        for (WarnBean data : datas) {
            vectors.add(data.getTableCol());
        }
        warnTableModel.addDatas(vectors);
    }

    private @Resource
    WarnTableModel warnTableModel;
    private JTable table;

    private JPanel center;

    private Vector<WarnBean> getSelectInfos() {
        int[] rows = table.getSelectedRows();
        Vector<WarnBean> datas = new Vector<>();
        for (int row : rows) {
            WarnBean data = new WarnBean();
            data.resolveTotalInfoTable(table, row);
            datas.add(data);
        }
        return datas;
    }

    private Vector<WarnBean> getAllInfos() {
        Vector<WarnBean> datas = new Vector<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            WarnBean data = new WarnBean();
            data.resolveTotalInfoTable(table, i);
            datas.add(data);
        }
        return datas;
    }

    private DataSearchPara para;

    private void getSearchPara() {
        // 物理量
        this.para = new DataSearchPara();
        Date t1 = c1.getTime();
        Date t2 = c2.getTime();
        if (t1 != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(t1);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            c1.set(Calendar.MILLISECOND, 0);
            para.setT1(c1.getTime());
            System.out.println(t1);
        }
        if (t2 != null) {
            Calendar c2 = Calendar.getInstance();
            c2.setTime(t2);
            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);
            c2.set(Calendar.MILLISECOND, 999);
            para.setT2(c2.getTime());
            System.out.println(t2);
        }
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
        tcr.initializeTable(table);
    }

    @Override
    public void refreashData() {
        if (para == null) {
            return;
        }
        card.show(center, "wait");
        new Thread(() -> {
            List<WarnBean> warns = warnService.getWarnByPara(para);
            addData(warns);
            card.show(center, "table");
        }).start();
    }
}
