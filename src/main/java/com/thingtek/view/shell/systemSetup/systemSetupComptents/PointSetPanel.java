package com.thingtek.view.shell.systemSetup.systemSetupComptents;

import com.thingtek.beanServiceDao.clt.entity.CltBean;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.PointTableModel;
import com.thingtek.view.shell.DataPanel;
import com.thingtek.view.shell.dataCollect.base.BaseCollectPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Vector;

public class PointSetPanel extends BaseSystemPanel {

    private int clttype = -1;
    @Resource
    private PointTableModel tablemodel;

    //测点表
    private JTable table;

    @Override
    public PointSetPanel init() {
        super.init();
        initializeTable();
        return this;
    }

    @Override
    protected void initTitle() {
        super.initTitle();
    }

    private JComboBox<String> clttypes;

    @Override
    protected void initCenter() {
        super.initCenter();

        JPanel center = new JPanel(new BorderLayout());
        addCenter(center);

        table = new JTable(tablemodel);

        JPanel centertitle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centertitle.setBackground(factorys.getColorFactory().getColor("centertitle"));
        clttypes = new JComboBox<>(cltService.getCltNames());
        clttypes.setSelectedItem(null);
        clttypes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CltBean clt = cltService.getCltByName((String) clttypes.getSelectedItem());
                clttype = clt.getType_num();
                refreshPoint();
                setEnable(true);
            }
        });
        center.add(centertitle, BorderLayout.NORTH);
        centertitle.add(new JLabel("类型:"));
        centertitle.add(clttypes);

        JScrollPane jspPoint = new JScrollPane(table);
        center.add(jspPoint, BorderLayout.CENTER);


    }

    private EditButton add;
    private EditButton delete;
    private EditButton apply;

    @Override
    protected void initToolbar() {
        super.initToolbar();

        add = addTool("添加测点", "add");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopEditing();
                int pointnum = pointService.getUnHasPointNum(clttype);
                if (pointnum == -1) {
                    errorMessage("添加失败,请稍后重试!");
                    return;
                }
                refreshPoint();
            }
        });
        delete = addTool("删除测点", "delete");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopEditing();
                int[] rows = table.getSelectedRows();
                if (rows.length <= 0) {
                    errorMessage("请先选择测点!");
                    return;
                }
                int[] pointnums = new int[rows.length];
                for (int i = 0; i < rows.length; i++) {
                    int pointnum = (Integer) table.getValueAt(rows[i], 1);
                    pointnums[i] = pointnum;
                }
                pointService.deleteByNums(clttype, pointnums);
                refreshPoint();
            }
        });
        apply = addTool("保存", "apply");
        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopEditing();
                if (!checkinput()) {
                    refreshPoint();
                    return;
                }
                for (int i = 0; i < table.getRowCount(); i++) {
                    int point_num = (int) table.getValueAt(i, 1);
                    String point_name = (String) table.getValueAt(i, 2);
                    int place_value = (int) table.getValueAt(i, 3);
                    PointBean pointBean = pointService.getPointByNum(clttype, point_num);
                    pointBean.setPoint_name(point_name);
                    pointBean.setPlace_value(place_value);
                }
                if (pointService.updatePoint(clttype)) {
                    successMessage("保存成功");
                    refreshPoint();
                }
            }
        });
        setEnable(false);
    }

    @Override
    public void loadingData() {

    }

    private void setEnable(boolean flag) {
        add.setEnabled(flag);
        delete.setEnabled(flag);
        apply.setEnabled(flag);
    }

    private void refreshPoint() {
        List<PointBean> points = pointService.getPoints(clttype);
        Vector<Vector<Object>> vectors = new Vector<>();
        for (PointBean point : points) {
            vectors.add(point.getTableCol());
        }
        tablemodel.addDatas(vectors);
        for (BaseCollectPanel collectPanel : logoInfo.getCollectPanelMap().values()) {
            if (collectPanel.getClttype() == clttype) {
                collectPanel.refreshPoint();
            }
        }
        for (DataPanel dataPanel : logoInfo.getDataPanels()) {
            dataPanel.refreashData();
        }
    }

    private void stopEditing() {
        if (table.isEditing())
            table.getCellEditor().stopCellEditing();
    }

    private boolean checkinput() {
        Vector<PointBean> points = new Vector<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            PointBean pointBean = new PointBean();
            int point_num = (int) table.getValueAt(i, 1);
            String point_name = (String) table.getValueAt(i, 2);
            if (point_name == null) {
                errorMessage("请输入测点 " + point_num + " 名称");
                return false;
            }
            pointBean.setPoint_num(point_num);
            pointBean.setPoint_name(point_name);
            pointBean.setClt_type(clttype);
            for (PointBean point : points) {
                if (point.getPoint_name().equals(point_name)) {
                    errorMessage("测点 " + point.getPoint_num() + " 与测点 " + pointBean.getPoint_num() + " 名称重复,请重新输入");
                    return false;
                }
            }
            points.add(pointBean);
        }
        return true;
    }

    @Resource
    private TCR tcr;

    private void initializeTable() {
        tcr.initializeTable(table);
    }
}
