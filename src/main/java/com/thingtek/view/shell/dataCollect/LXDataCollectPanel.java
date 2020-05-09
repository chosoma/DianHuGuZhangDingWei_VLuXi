package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.CollectWarnTableModel;
import com.thingtek.view.shell.BasePanel;
import com.thingtek.view.shell.DataPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * 数据采集
 */

public class LXDataCollectPanel extends BasePanel implements DataPanel {


    @Override
    public LXDataCollectPanel init() {
        setLayout(new BorderLayout());
        setOpaque(false);
        initCompnent();
        initBottom();
        refreashData();
//        refreashWarn();
        return this;
    }

    private @Resource
    CollectWarnTableModel tableModel;

    private JTable warntable;

    private void initBottom() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        warntable = new JTable(tableModel);
        JScrollPane jScrollPane = new JScrollPane(warntable);
        bottomPanel.add(jScrollPane, BorderLayout.CENTER);
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 100));
        this.add(bottomPanel, BorderLayout.SOUTH);
        /*java.util.List<WarnBean> warnBeanList = warnService.getLastWarn();
        java.util.List<Vector<Object>> vectors = new ArrayList<>();
        for (WarnBean warn : warnBeanList) {
            vectors.add(warn.getTableCol());
        }
        tableModel.addDatas(vectors);*/
        initializeTable();
    }

    @Resource
    private TCR tcr;
    private JTabbedPane jTabbedPane;

    private void initializeTable() {
        tcr.initializeTable(warntable);
    }

    private void initCompnent() {
        jTabbedPane = new JTabbedPane();
        jTabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
        add(jTabbedPane, BorderLayout.CENTER);
        refreshPoint();

    }

    public void addtablewarn(WarnBean warnBean) {
        tableModel.addData(warnBean.getCollectTableCol());
    }

    public void refreshPoint() {
        jTabbedPane.removeAll();
        java.util.List<PipeBean> pipes = pipeService.findAll();
        for (PipeBean pipe : pipes) {
            for (int i = 1; i <= pipe.getPipe_page(); i++) {
                String name = pipe.getPipe_name() + "管_" + i+"段";
                LXCollectPanel clt = new LXCollectPanel();
                clt.setAdmin(true);
                clt.setName(name);
                jTabbedPane.add(clt);
                clt.setFactorys(factorys);
                clt.setUnitService(unitService);
                java.util.List<LXUnitBean> baseunits = unitService.getUnitsByPipe(pipe);
                java.util.List<LXUnitBean> units = new ArrayList<>();
                for (LXUnitBean baseunit : baseunits) {
                    if (baseunit.getPipe_page() == i) {
                        units.add(baseunit);
                    }
                }
                clt.setUnits(units);
                clt.init();
            }
        }
    }

    @Override
    public void refreashData() {
        refreshPoint();
    }

    public void showWarn() {


    }
}
