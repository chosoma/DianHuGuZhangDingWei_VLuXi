package com.thingtek.view.shell.systemSetup.systemSetupComptents;

import com.thingtek.beanServiceDao.clt.entity.CltBean;
import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.config.clazz.ClazzConfig;
import com.thingtek.socket.CollectServer;
import com.thingtek.socket.agreement.SocketAgreement;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.component.dialog.AddUnitDialog;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.BaseTableModel;
import com.thingtek.view.component.tablemodel.LXUnitTableModel;
import com.thingtek.view.component.tablemodel.TableConfig;
import com.thingtek.view.shell.DataPanel;
import com.thingtek.view.shell.Shell;
import com.thingtek.view.shell.dataCollect.base.BaseCollectPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class LXUnitSetPanel extends BaseSystemPanel {
    private int clttype = 4;

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
    @Resource
    private LXUnitTableModel tableModel;
    @Resource
    private TableConfig tableConfig;

    private JComboBox<String> clttypes;
    @Resource
    private ClazzConfig clazzConfig;

    @Override
    protected void initCenter() {
        super.initCenter();
        table = new JTable();
        table.setModel(tableModel);

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

//                refreshUnit();
            }
        });
        centertitle.add(clttypes);
        EditButton refresh = new EditButton("刷新", factorys.getIconFactory().getIcon("refresh"));
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
//        center.add(centertitle, BorderLayout.NORTH);
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
        EditButton refresh = addTool("刷新", "refresh");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshUnit();
            }
        });
        add = addTool("添加", "add");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clttype == -1) {
                    return;
                }
                stopEditing();
                AddUnitDialog unitDialog = new AddUnitDialog(shell, "添加", factorys.getIconFactory().getImage("set"));
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

        delete = addTool("删除", "delete");
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
                unitService.deleteUnitByNum(clttype, unitnums);
                refreshUnit();
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
                    unit.resolveLXTable(table, i);
                    String pipe_name = (String) table.getValueAt(i, 1);
                    PipeBean pipe = pipeService.getPipeByName(pipe_name);
                    if (pipe != null) {
                        unit.setPipe(pipe);
                        unit.setPipe_id(pipe.getPipe_id());
                    }
                    unit.resolve2map();
                    units.add(unit);
                }
                if (unitService.updateUnit(clttype, units.toArray(new BaseUnitBean[0]))) {
                    successMessage("保存成功");
                }
                refreshUnit();
            }
        });
//        setEnable(false);
    }

    private JComboBox<String> jcbpipenames;

    @Override
    public void loadingData() {
        JComboBox<Integer> jcbpages = new JComboBox<>();
        jcbpipenames = new JComboBox<>(pipeService.getPipeNames());
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(jcbpipenames));
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(jcbpages));
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                PipeBean pipeBean = pipeService.getPipeByName((String) table.getValueAt(table.getSelectedRow(), 1));
                int pipe_page = pipeBean.getPipe_page();
                Vector<Integer> vector = new Vector<>();
                for (int i = 1; i <= pipe_page; i++) {
                    vector.add(i);
                }
                jcbpages.setModel(new DefaultComboBoxModel<>(vector));
            }
        });
        jcbpipenames.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                PipeBean pipeBean = pipeService.getPipeByName((String) e.getItem());
                int pipe_page = pipeBean.getPipe_page();
                Vector<Integer> vector = new Vector<>();
                for (int i = 1; i <= pipe_page; i++) {
                    vector.add(i);
                }
                jcbpages.setModel(new DefaultComboBoxModel<>(vector));
            }
        });
        refreshUnit();
    }

    private void setEnable(boolean flag) {
        add.setEnabled(flag);
        delete.setEnabled(flag);
        apply.setEnabled(flag);
    }

    public void refreshUnit() {
        stopEditing();
        List<BaseUnitBean> units = unitService.getAll(clttype);
        List<Vector<Object>> vectors = new ArrayList<>();
        for (BaseUnitBean unit : units) {
            Vector<Object> vector = unit.getLXTableCol();
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

        jcbpipenames.setModel(new DefaultComboBoxModel<>(pipeService.getPipeNames()));

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
