package com.thingtek.view.shell.systemSetup.systemSetupComptents;

import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.PipeTableModel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PipeSetPanel extends BaseSystemPanel {


    @Resource
    private PipeTableModel tablemodel;

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

        refreshPipe();

        JScrollPane jspPoint = new JScrollPane(table);
        center.add(jspPoint, BorderLayout.CENTER);

    }

    private EditButton add;
    private EditButton delete;
    private EditButton apply;

    @Override
    protected void initToolbar() {
        super.initToolbar();

        add = addTool("添加", "add");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopEditing();
                int pipeid = pipeService.getUnHasPointNum();
                if (pipeid == -1) {
                    errorMessage("添加失败,请稍后重试!");
                    return;
                }
                refreshPipe();
            }
        });
        delete = addTool("删除", "delete");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });
        apply = addTool("保存", "apply");
        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopEditing();
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

            }
        });
    }

    @Override
    public void loading() {

    }

    private void setEnable(boolean flag) {
        add.setEnabled(flag);
        delete.setEnabled(flag);
        apply.setEnabled(flag);
    }

    private void refreshPipe() {
        List<PipeBean> pipes = pipeService.findAll();
        List<Vector<Object>> vectors = new ArrayList<>();
        for (PipeBean pipe : pipes) {
            vectors.add(pipe.getDataTotalCol());
        }
        tablemodel.addDatas(vectors);
    }

    private void stopEditing() {
        if (table.isEditing())
            table.getCellEditor().stopCellEditing();
    }

    private boolean checkinput() {
        Vector<PointBean> points = new Vector<>();
        for (int i = 0; i < table.getRowCount(); i++) {

        }
        return true;
    }

    @Resource
    private TCR tcr;

    private void initializeTable() {
        tcr.initializeTable(table);
    }
}
