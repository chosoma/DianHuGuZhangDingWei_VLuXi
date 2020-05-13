package com.thingtek.view.shell.systemSetup.systemSetupComptents;

import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.component.dialog.AddLXUnitDialog;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.LXUnitTableModel;
import com.thingtek.view.shell.DataPanel;
import com.thingtek.view.shell.Shell;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class LXUnitSetPanel extends BaseSystemPanel {

    @Resource
    private Shell shell;

    private JTable table;
    @Resource
    private LXUnitTableModel tableModel;

    @Override
    protected void initCenter() {
        super.initCenter();
        table = new JTable();
        table.setModel(tableModel);

        JPanel center = new JPanel(new BorderLayout());
        addCenter(center);

        JScrollPane jspTable = new JScrollPane(table);
        center.add(jspTable, BorderLayout.CENTER);
        initializeTable();
    }

    private EditButton add, delete, apply;

    @Override
    protected void initToolbar() {
        super.initToolbar();
        EditButton refresh = addTool("刷新", "refresh");
        refresh.addActionListener(e -> refreshUnit());
        add = addTool("添加", "add");
        add.addActionListener(e -> {
            stopEditing();
            AddLXUnitDialog unitDialog = new AddLXUnitDialog(shell, "添加", factorys.getIconFactory().getImage("set"));
            unitDialog.setFactorys(factorys);
            unitDialog.setUnitService(unitService);
            unitDialog.setPipeService(pipeService);
            unitDialog.initDialog().visible();
            unitDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    refreshUnit();
                }
            });
        });

        delete = addTool("删除", "delete");
        delete.addActionListener(e -> {
            stopEditing();
            int[] rows = table.getSelectedRows();
            if (rows.length <= 0) {
                errorMessage("请先选择单元!");
                return;
            }
            short[] unitnums = new short[rows.length];
            for (int i = 0; i < rows.length; i++) {
                Short unitnum = (Short) table.getValueAt(rows[i], 0);
                unitnums[i] = unitnum;
            }
            unitService.deleteUnitByNum(unitnums);
            refreshUnit();
        });
        apply = addTool("保存", "apply");
        apply.addActionListener(e -> {
            stopEditing();
            if (table.isEditing()) {
                errorMessage("您有内容输入有误!");
                return;
            }
            if (!checkinput()) {
                refreshUnit();
                return;
            }
            List<LXUnitBean> units = new ArrayList<>();
            for (int i = 0; i < table.getRowCount(); i++) {
                LXUnitBean unit = unitService.getUnitByNumber((Short) table.getValueAt(i, 0));
                String pipe_name = (String) table.getValueAt(i, 1);
                PipeBean pipe = pipeService.getPipeByName(pipe_name);
                if (pipe != null) {
                    unit.setPipe(pipe);
                }
                unit.resolveLXTable(table, i);

                units.add(unit);
            }
            if (unitService.updateLXUnit(units.toArray(new LXUnitBean[0]))) {
                successMessage("保存成功");
            }
            refreshUnit();
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

    private JComboBox<String> jcbpipenames;
    private JComboBox<Integer> jcbpages;

    @Override
    public void loadingData() {
        jcbpages = new JComboBox<>();
        jcbpipenames = new JComboBox<>(pipeService.getPipeNames());
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(jcbpipenames));
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(jcbpages));
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                refreshPages();
            }
        });
        jcbpipenames.addItemListener(e -> refreshPages());
        refreshUnit();
    }

    @Override
    public void refreshTable() {
        refreshUnit();
    }

    private void refreshPages() {
        PipeBean pipeBean = pipeService.getPipeByName((String) table.getValueAt(table.getSelectedRow(), 1));
        int pipe_page = pipeBean.getPipe_page();
        Vector<Integer> vector = new Vector<>();
        for (int i = 1; i <= pipe_page; i++) {
            vector.add(i);
        }
        jcbpages.setModel(new DefaultComboBoxModel<>(vector));
    }

    public void refreshUnit() {
        stopEditing();
        List<LXUnitBean> units = unitService.getAll();
        List<Vector<Object>> vectors = new ArrayList<>();
        for (LXUnitBean unit : units) {
            vectors.add(unit.getLXTableCol());
        }
        tableModel.addDatas(vectors);
        jcbpipenames.setModel(new DefaultComboBoxModel<>(pipeService.getPipeNames()));

        for (DataPanel dataPanel : logoInfo.getDataPanels()) {
            dataPanel.refreashData();
        }
    }

    private void stopEditing() {
        if (table.isEditing())
            table.getCellEditor().stopCellEditing();
    }


    private boolean checkinput() {

        return true;
    }

    @Resource
    private TCR tcr;

    private void initializeTable() {
        tcr.initializeTable(table);
    }


}
