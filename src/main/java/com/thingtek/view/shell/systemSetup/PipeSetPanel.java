package com.thingtek.view.shell.systemSetup;

import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.PipeTableModel;
import com.thingtek.view.shell.base.BaseSystemPanel;
import com.thingtek.view.shell.dataCollect.LXDataCollectPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PipeSetPanel extends BaseSystemPanel {

    @Resource
    private PipeTableModel tablemodel;
    @Resource
    private LXDataCollectPanel lxDataCollectPanel;

    //测点表
    private JTable table;

    @Override
    public PipeSetPanel init() {
        super.init();
        initializeTable();
        return this;
    }

    @Override
    protected void initTitle() {
        super.initTitle();
    }

    @Override
    protected void initCenter() {
        super.initCenter();
        JPanel center = new JPanel(new BorderLayout());
        addCenter(center);
        table = new JTable(tablemodel);
        JPanel centertitle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centertitle.setBackground(factorys.getColorFactory().getColor("centertitle"));
        JScrollPane jspPoint = new JScrollPane(table);
        center.add(jspPoint, BorderLayout.CENTER);
    }

    private EditButton add, delete, apply;

    @Override
    protected void initToolbar() {
        super.initToolbar();

        add = addTool("添加", "add");
        add.addActionListener(e -> {
            stopEditing();
            int pipeid = pipeService.getUnHasPipeId();
            if (pipeid == -1) {
                errorMessage("添加失败,请稍后重试!");
                return;
            }
            refreshPipe();
        });
        delete = addTool("删除", "delete");
        delete.addActionListener(e -> {
            stopEditing();
            int[] rows = table.getSelectedRows();
            if (rows.length <= 0) {
                errorMessage("请先选择!");
                return;
            }
            int[] pointnums = new int[rows.length];
            for (int i = 0; i < rows.length; i++) {
                int pointnum = (Integer) table.getValueAt(rows[i], 0);
                pointnums[i] = pointnum;
            }
            pipeService.delete(pointnums);
            refreshPipe();
        });
        apply = addTool("保存", "apply");
        apply.addActionListener(e -> {
            stopEditing();
            if (table.isEditing()) {
                errorMessage("您有内容输入有误!");
                return;
            }
            if (!checkinput()) {
                refreshPipe();
                return;
            }

            for (int i = 0; i < table.getRowCount(); i++) {
                int pipe_id = (int) table.getValueAt(i, 0);
                String pipe_name = (String) table.getValueAt(i, 1);
                int pipe_page = (int) table.getValueAt(i, 2);
                PipeBean pipe = pipeService.getPipeById(pipe_id);
                pipe.setPipe_name(pipe_name);
                pipe.setPipe_page(pipe_page);
            }
            if (pipeService.update()) {
                successMessage("保存成功");
                refreshPipe();
            }

        });
        if (!logoInfo.isAdmin()) {
            visibleall();
        }
    }

    private void visibleall() {
        apply.setVisible(false);
        add.setVisible(false);
        delete.setVisible(false);
    }


    @Override
    public void loadingData() {
        refreshPipe();
    }

    @Override
    public void refreshTable() {
        refreshPipe();
    }

    private void refreshPipe() {
        List<PipeBean> pipes = pipeService.findAll();
        List<Vector<Object>> vectors = new ArrayList<>();
        for (PipeBean pipe : pipes) {
            vectors.add(pipe.getDataTotalCol());
        }
        tablemodel.addDatas(vectors);
        lxDataCollectPanel.refreshPoint();
    }

    private void stopEditing() {
        if (table.isEditing())
            table.getCellEditor().stopCellEditing();
    }

    private boolean checkinput() {
        for (int i = 0; i < table.getRowCount(); i++) {
            int id = (int) table.getValueAt(i, 0);
            Object object = table.getValueAt(i, 2);
            String strnum = String.valueOf(object);
            if (!isInt(strnum)) {
                errorMessage("管体 " + id + " 分段请输入整数!");
                return false;
            }
        }
        return true;
    }

    @Resource
    private TCR tcr;

    private void initializeTable() {
        tcr.initializeTable(table);
    }
}
