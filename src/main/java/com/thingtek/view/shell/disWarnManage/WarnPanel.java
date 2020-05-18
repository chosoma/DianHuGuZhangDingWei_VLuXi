package com.thingtek.view.shell.disWarnManage;

import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.socket.data.entity.DataSearchPara;
import com.thingtek.view.component.panel.Check2SPinner;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.WarnTableModel;
import com.thingtek.view.shell.base.BasePanel;
import com.thingtek.view.shell.base.DataPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * 增加查询分页
 */
public class WarnPanel extends BasePanel implements DataPanel {

    @Override
    public WarnPanel init() {
        setLayout(new BorderLayout());
        initCenter();
        initBottom();
        return this;
    }

    private Check2SPinner c1;
    private Check2SPinner c2;
    private JButton search;
    private JButton delete;
    private JButton clear;
    private CardLayout card;

    private void initBottom() {
        JPanel bottom = new JPanel();
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
            if (warnService.deleteWarn(datas.toArray(new WarnBean[0]))) {
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
            if (warnService.deleteWarn(datas.toArray(new WarnBean[0]))) {
                successMessage("清空成功");
                search.doClick();
            } else {
                falseMessage("清空失败");
            }
        });
        if (isAdmin()) {
            bottom.add(delete);
            bottom.add(clear);
        }
        this.add(bottom, BorderLayout.SOUTH);
    }

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

    @Resource
    private WarnTableModel warnTableModel;
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
