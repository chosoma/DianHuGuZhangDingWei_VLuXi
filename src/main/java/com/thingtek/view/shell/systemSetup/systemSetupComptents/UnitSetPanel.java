package com.thingtek.view.shell.systemSetup.systemSetupComptents;

import com.thingtek.beanServiceDao.clt.entity.CltBean;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.socket.agreement.SocketAgreement;
import com.thingtek.config.clazz.ClazzConfig;
import com.thingtek.socket.CollectServer;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.component.dialog.AddUnitDialog;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.BaseTableModel;
import com.thingtek.view.component.tablemodel.TableConfig;
import com.thingtek.view.shell.DataPanel;
import com.thingtek.view.shell.Shell;
import com.thingtek.view.shell.dataCollect.base.BaseCollectPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class UnitSetPanel extends BaseSystemPanel {
    private int clttype = -1;

    public int getClttype() {
        return clttype;
    }

    @Resource
    private Shell shell;
    @Resource
    private CollectServer server;
    @Resource
    private SocketAgreement agreement;

    private JTable table;

    private BaseTableModel tableModel;
    @Resource
    private TableConfig tableConfig;

    private JComboBox<String> clttypes;
    @Resource
    private ClazzConfig clazzConfig;

    @Override
    protected void initCenter() {
        super.initCenter();
        table = new JTable();

        JPanel center = new JPanel(new BorderLayout());
        addCenter(center);
        JPanel centertitle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centertitle.setBackground(factorys.getColorFactory().getColor("centertitle"));
        centertitle.add(new JLabel("单元类型:"));
        clttypes = new JComboBox<>(cltService.getCltNames());
        clttypes.setSelectedItem(null);
        clttypes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CltBean clt = cltService.getCltByName((String) clttypes.getSelectedItem());
                clttype = clt.getType_num();
                setEnable(true);
                switch (clttype) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        break;
                }
                tableModel = tableConfig.getUnitModel(clt.getType_name());
                table.setModel(tableModel);
                for (int i = 0; i < table.getColumnCount(); i++) {
                    TableColumn tableColumn = table.getColumnModel().getColumn(i);
                    JComboBox<String> comboBox;
                    switch (i) {
                        case 1:
                            comboBox = new JComboBox<>(pointService.getPointNames(clttype));
                            tableColumn.setCellEditor(new DefaultCellEditor(comboBox));
                            break;
                        case 2:
                            comboBox = new JComboBox<>(new String[]{"A", "B", "C"});
                            tableColumn.setCellEditor(new DefaultCellEditor(comboBox));
                            break;
                        case 3:
                            break;
                    }
                }
                refreshUnit();
            }
        });
        centertitle.add(clttypes);
        EditButton refresh = new EditButton("刷新",factorys.getIconFactory().getIcon("refresh"));
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clttype == -1) {
                    return;
                }
                refreshUnit();
            }
        });
        centertitle.add(refresh);
        center.add(centertitle, BorderLayout.NORTH);
        JScrollPane jspTable = new JScrollPane(table);
        center.add(jspTable, BorderLayout.CENTER);
        initializeTable();
    }

    private EditButton add;
    private EditButton delete;
    private EditButton apply;

    @Override
    protected void initToolbar() {
        super.initToolbar();
        add = addTool("添加单元", "add");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clttype == -1) {
                    return;
                }
                stopEditing();
                AddUnitDialog unitDialog = new AddUnitDialog(shell, "添加单元", factorys.getIconFactory().getImage("set"));
                unitDialog.setFactorys(factorys);
                unitDialog.setUnitService(unitService);
                unitDialog.setPointService(pointService);
                unitDialog.setClazzConfig(clazzConfig);
                unitDialog.setClttype(clttype);
                unitDialog.initDialog().visible();
                unitDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        refreshUnit();
                    }
                });
            }
        });

        delete = addTool("删除单元", "delete");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clttype == -1) {
                    return;
                }
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
                if (unitService.deleteUnitByNum(clttype, unitnums)) {
                    successMessage("删除成功!");
                    refreshUnit();
                } else {
                    falseMessage("删除失败!");
                }
            }
        });
        apply = addTool("保存", "apply");
        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clttype == -1) {
                    return;
                }
                stopEditing();
                if (!checkinput()) {
                    refreshUnit();
                    return;
                }
                List<BaseUnitBean> units = new ArrayList<>();
                for (int i = 0; i < table.getRowCount(); i++) {
                    BaseUnitBean unit = unitService.getUnitByNumber(clttype, (Short) table.getValueAt(i, 0));
                    unit.resolveTable(table, i);
                    String point_name = (String) table.getValueAt(i, 1);
                    PointBean point = pointService.getPointByName(clttype, point_name);
                    if (point != null) {
                        unit.setPoint(point);
                        unit.setPoint_num(point.getPoint_num());
                    }
                    unit.resolve2map();
                    units.add(unit);
                }
                if (unitService.updateUnit(clttype, units.toArray(new BaseUnitBean[0]))) {
                    successMessage("保存成功");
                    refreshUnit();
                } else {
                    falseMessage("保存失败");
                }
            }
        });
        setEnable(false);
    }

    @Override
    public void loading() {

    }

    private void setEnable(boolean flag) {
        add.setEnabled(flag);
        delete.setEnabled(flag);
        apply.setEnabled(flag);
    }

    public void refreshUnit() {
        List<BaseUnitBean> units = unitService.getAll(clttype);
        List<Vector<Object>> vectors = new ArrayList<>();
        for (BaseUnitBean unit : units) {
            Vector<Object> vector = unit.getTableCol();
            vectors.add(vector);
        }
        tableModel.addDatas(vectors);
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

        return true;
    }

    @Resource
    private TCR tcr;

    private void initializeTable() {
        tcr.initializeTable(table);
    }


}
