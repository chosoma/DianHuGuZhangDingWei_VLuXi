package com.thingtek.view.shell.systemSetup.systemSetupComptents;

import com.thingtek.beanServiceDao.clt.entity.CltBean;
import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import com.thingtek.config.PortConfig;
import com.thingtek.socket.CollectServer;
import com.thingtek.socket.CollectSocket;
import com.thingtek.socket.agreement.SocketAgreement;
import com.thingtek.socket.entity.BaseS2G;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.DisUnitAdminTableModel;
import com.thingtek.view.component.tablemodel.DisUnitAdminTotalTableModel;
import com.thingtek.view.shell.DataPanel;
import com.thingtek.view.shell.dataCollect.base.BaseCollectPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class UnitAdminSetPanel extends BaseSystemPanel {
    private int clttype = -1;

    public int getClttype() {
        return clttype;
    }

    @Resource
    private CollectServer server;
    @Resource
    private SocketAgreement agreement;

    private JTable onlytable;
    private JTable totaltable;
    @Resource
    private DisUnitAdminTableModel tableModel;
    @Resource
    private DisUnitAdminTotalTableModel totalTableModel;

    private JComboBox<String> clttypes;
    @Resource
    private PortConfig portConfig;

    @Override
    protected void initCenter() {
        super.initCenter();
        onlytable = new JTable();
        totaltable = new JTable();

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
                onlytable.setModel(tableModel);
                totaltable.setModel(totalTableModel);
                refreshUnit();
                setEnable(true);
            }
        });
        centertitle.add(clttypes);
        EditButton refresh = new EditButton("刷新", factorys.getIconFactory().getIcon("refresh"));
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshUnit();
            }
        });
        centertitle.add(refresh);

        JLabel jlmcip = new JLabel("脉冲IP:");
        centertitle.add(jlmcip);
        jtfMCIP = new JTextField();
        jtfMCIP.setPreferredSize(new Dimension(100, 20));
        jtfMCIP.setText(portConfig.getMcip());
        centertitle.add(jtfMCIP);
        JLabel jlmc = new JLabel("脉冲间隔:");
        centertitle.add(jlmc);
        jtfMC = new JTextField();
        jtfMC.setPreferredSize(new Dimension(50, 20));
        centertitle.add(jtfMC);
        setmc = new EditButton("设置脉冲间隔", factorys.getIconFactory().getIcon("set"));
        setmc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strmc = jtfMC.getText();
                boolean flag = isInt(strmc);
                if (!flag) {
                    errorMessage("脉冲输入有误!");
                    return;
                }
                int mc = Integer.parseInt(strmc);
                try {
                    String mcip = jtfMCIP.getText();
                    if (!isIp(mcip)) {
                        errorMessage("脉冲IP输入有误!");
                        return;
                    }
                    portConfig.setMcip(mcip);
                    portConfig.refreshConfigXml();
                    CollectSocket socket = server.getSocket(mcip);
                    if (socket == null) {
                        falseMessage("脉冲板离线!");
                        return;
                    }
                    BaseS2G s2g = agreement.getS2G("setmc");
                    byte[] bytes = new byte[2];
                    bytes[0] = (byte) (mc / 60);
                    bytes[1] = (byte) (mc % 60);
                    s2g.setDatas(bytes);
                    socket.sendMSG(s2g.getResult());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        centertitle.add(setmc);
        searchmc = new EditButton("查询脉冲间隔", factorys.getIconFactory().getIcon("search"));
        searchmc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String mcip = jtfMCIP.getText();
                    if (!isIp(mcip)) {
                        errorMessage("脉冲IP输入有误!");
                        return;
                    }
                    portConfig.setMcip(mcip);
                    portConfig.refreshConfigXml();
                    CollectSocket socket = server.getSocket(mcip);
                    if (socket == null) {
                        falseMessage("脉冲板离线!");
                        return;
                    }
                    BaseS2G s2g = agreement.getS2G("searchmc");
                    socket.sendMSG(s2g.getResult());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        centertitle.add(searchmc);
        center.add(centertitle, BorderLayout.NORTH);

        JPanel tablepanel = new JPanel(new BorderLayout());
        JScrollPane jspTable = new JScrollPane(onlytable);
        tablepanel.add(jspTable, BorderLayout.CENTER);

        centerPanel = new JPanel(cardLayout);
        centerPanel.add(tablepanel, "only");

        JPanel totaltablepanel = new JPanel(new BorderLayout());
        JScrollPane jsptotalTable = new JScrollPane(totaltable);
        totaltablepanel.add(jsptotalTable, BorderLayout.CENTER);
        centerPanel.add(totaltablepanel, "total");

        center.add(centerPanel, BorderLayout.CENTER);
        initializeTable();
    }

    private JPanel centerPanel;
    private CardLayout cardLayout = new CardLayout();
    private boolean centerflag = false;
    private JTextField jtfMCIP;
    private EditButton totalset;
    private EditButton apply;
    private EditButton setfz;
    private JTextField jtftotalfz;
    private EditButton settotalfz;
    private JTextField jtfMC;
    private EditButton setmc;
    private EditButton searchmc;
    private EditButton setfdbs;
    private JTextField jtftotalfdbs;
    private EditButton settotalfdbs;

    @Override
    protected void initToolbar() {
        super.initToolbar();
        totalset = addTool("批量设置", "set");
        totalset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (centerflag) {
                    cardLayout.show(centerPanel, "only");
                    totalset.setText("批量设置");
                    centerflag = false;
                } else {
                    cardLayout.show(centerPanel, "total");
                    totalset.setText("单个设置");
                    centerflag = true;
                }
                refreshVisible();
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
                for (int i = 0; i < onlytable.getRowCount(); i++) {
                    BaseUnitBean unit = unitService.getUnitByNumber(clttype, (Short) onlytable.getValueAt(i, 0));
                    unit.resolveAdminTable(onlytable, i);
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

        setfz = addTool("设置阈值", "set");
        setfz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clttype == -1) {
                    return;
                }
                stopEditing();
                int row = onlytable.getSelectedRow();
                int[] rows = onlytable.getSelectedRows();
                if (rows.length <= 0) {
                    errorMessage("请先选择单元!");
                    return;
                }
                DisUnitBean unit = (DisUnitBean) unitService.getUnitByNumber(clttype, (Short) onlytable.getValueAt(row, 0));
                if (!check1input(row)) {
                    refreshUnit();
                    return;
                }
                unit.resolveAdminTable(onlytable, row);
                CollectSocket socket = server.getSocket(unit.getIp(), unit.getPort());
                if (socket == null) {
                    falseMessage("未选择单元或选择单元未连接!");
                    return;
                }
                BaseS2G s2g = agreement.getS2G("setfz");
                s2g.setUnitnum(unit.getUnit_num());
                Integer fz = unit.getFz();
                byte[] bytes = new byte[2];
                bytes[0] = (byte) (fz & 0xff);
                bytes[1] = (byte) ((fz >> 8) & 0xff);
                s2g.setDatas(bytes);
                try {
                    socket.sendMSG(s2g.getResult());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                unitService.updateUnit(clttype, unit);
            }
        });

        setfdbs = addTool("设置放大倍数", "set");
        setfdbs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clttype == -1) {
                    return;
                }
                stopEditing();
                int row = onlytable.getSelectedRow();
                int[] rows = onlytable.getSelectedRows();
                if (rows.length <= 0) {
                    errorMessage("请先选择单元!");
                    return;
                }
                DisUnitBean unit = (DisUnitBean) unitService.getUnitByNumber(clttype, (Short) onlytable.getValueAt(row, 0));
                if (!check1input(row)) {
                    refreshUnit();
                    return;
                }
                unit.resolveAdminTable(onlytable, row);
                CollectSocket socket = server.getSocket(unit.getIp(), unit.getPort());
                if (socket == null) {
                    falseMessage("未选择单元或选择单元未连接!");
                    return;
                }
                BaseS2G s2g = agreement.getS2G("setfdbs");
                s2g.setUnitnum(unit.getUnit_num());
                Integer fdbs = unit.getFdbs();
                byte[] bytes = new byte[1];
                int w = fdbs;
//                int w = (2050 / (fdbs - 10));
                bytes[0] = (byte) (w > 127 ? 127 : w < 13 ? 13 : w);
                s2g.setDatas(bytes);
                try {
                    socket.sendMSG(s2g.getResult());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                unitService.updateUnit(clttype, unit);
            }
        });
        jtftotalfz = new JTextField();
        jtftotalfz.setPreferredSize(new Dimension(40, 20));
        jtftotalfz.setVisible(false);
        addTool(jtftotalfz);
        settotalfz = addTool("设置阈值", "set");
        settotalfz.setVisible(false);
        settotalfz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strfz = jtftotalfz.getText();
                if (!checkfz(strfz)) {
                    errorMessage("阈值输入有误!(2000-4095)");
                    return;
                }
                int fz = Integer.parseInt(strfz);
                List<BaseUnitBean> units = unitService.getAll(4);
                for (BaseUnitBean baseunit : units) {
                    DisUnitBean unit = (DisUnitBean) baseunit;
                    CollectSocket socket = server.getSocket(unit.getIp(), unit.getPort());
                    BaseS2G s2g = agreement.getS2G("setfz");
                    s2g.setUnitnum(unit.getUnit_num());
                    byte[] bytes = new byte[2];
                    bytes[0] = (byte) (fz & 0xff);
                    bytes[1] = (byte) ((fz >> 8) & 0xff);
                    s2g.setDatas(bytes);
                    if (socket == null) {
                        continue;
                    }
                    try {
                        socket.sendMSG(s2g.getResult());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        jtftotalfdbs = new JTextField();
        jtftotalfdbs.setPreferredSize(new Dimension(40, 20));
        jtftotalfdbs.setVisible(false);
        addTool(jtftotalfdbs);
        settotalfdbs = addTool("设置放大倍数", "set");
        settotalfdbs.setVisible(false);
        settotalfdbs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strfdbs = jtftotalfdbs.getText();
                if (!checkfdbs(strfdbs)) {
                    errorMessage("放大倍数输入有误!(13-127)");
                    return;
                }
                int fdbs = Integer.parseInt(strfdbs);
                List<BaseUnitBean> units = unitService.getAll(4);
                for (BaseUnitBean baseunit : units) {
                    DisUnitBean unit = (DisUnitBean) baseunit;
                    CollectSocket socket = server.getSocket(unit.getIp(), unit.getPort());
                    BaseS2G s2g = agreement.getS2G("setfdbs");
                    s2g.setUnitnum(unit.getUnit_num());
                    byte[] bytes = new byte[2];
                    bytes[0] = (byte) (fdbs & 0xff);
                    bytes[1] = (byte) ((fdbs >> 8) & 0xff);
                    s2g.setDatas(bytes);
                    if (socket == null) {
                        continue;
                    }
                    try {
                        socket.sendMSG(s2g.getResult());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        setEnable(false);
    }

    @Override
    public void loadingData() {

    }

    public void refreshUnit() {
        List<BaseUnitBean> units = unitService.getAll(clttype);
        List<Vector<Object>> vectors = new ArrayList<>();
        for (BaseUnitBean unit : units) {
            Vector<Object> vector = unit.getAdminSetTableCol();
            vectors.add(vector);
        }
        tableModel.addDatas(vectors);
        totalTableModel.addDatas(vectors);
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
        if (onlytable.isEditing())
            onlytable.getCellEditor().stopCellEditing();
    }

    private boolean checkfz(String fz) {
        if (fz == null || fz.equals("") || isInt(fz)) {
            return false;
        }
        int fzint = Integer.parseInt(fz);
        return fzint >= 2000 && fzint <= 4095;
    }

    private boolean checkfdbs(String fdbs) {
        if (fdbs == null || fdbs.equals("") || isInt(fdbs)) {
            return false;
        }
        int fzint = Integer.parseInt(fdbs);
        return fzint >= 13 && fzint <= 127;
    }

    private boolean check1input(int row) {
        short unit_num = (short) onlytable.getValueAt(row, 0);
        Object objfz = onlytable.getValueAt(row, 1);
        String fz = objfz == null ? null : String.valueOf(objfz);

        if (!checkfz(fz)) {
            errorMessage("单元编号 " + unit_num + " 阈值输入有误!(2000-4095)");
            return false;
        }
        Object objfdbs = onlytable.getValueAt(row, 2);
        String fdbs = objfdbs == null ? null : String.valueOf(objfdbs);

        if (!checkfdbs(fdbs)) {
            errorMessage("单元编号 " + unit_num + " 放大倍数输入有误!(13-127)");
            return false;
        }
        String ip = (String) onlytable.getValueAt(row, 3);
        if (!(ip == null || isIp(ip))) {
            errorMessage("单元编号 " + unit_num + " IP地址输入有误");
            return false;
        }
        String port = (String) onlytable.getValueAt(row, 4);
        if (!(port == null || isNum(port))) {
            errorMessage("单元编号 " + unit_num + " IP地址输入有误");
            return false;
        }
        String place_value = (String) onlytable.getValueAt(row, 5);
        if (!(place_value == null || isNum(place_value))) {
            errorMessage("单元编号 " + unit_num + " 安装位置输入有误");
            return false;
        }
        return true;
    }

    private boolean checkinput() {
        List<Boolean> flags = new ArrayList<>();
        for (int i = 0; i < onlytable.getRowCount(); i++) {
            flags.add(check1input(i));
        }
        return !flags.contains(false);
    }

    @Resource
    private TCR tcr;

    private void initializeTable() {
        tcr.initializeTable(onlytable);
        tcr.initializeTable(totaltable);
    }

    private void refreshVisible() {
        if (centerflag) {
            apply.setVisible(false);
            setfz.setVisible(false);
            setfdbs.setVisible(false);
            jtftotalfz.setVisible(true);
            settotalfz.setVisible(true);
            jtftotalfdbs.setVisible(true);
            settotalfdbs.setVisible(true);
        } else {
            apply.setVisible(true);
            setfz.setVisible(true);
            setfdbs.setVisible(true);
            jtftotalfz.setVisible(false);
            settotalfz.setVisible(false);
            jtftotalfdbs.setVisible(false);
            settotalfdbs.setVisible(false);
        }
    }

    private void setEnable(boolean flag) {
        apply.setEnabled(flag);
        setfz.setEnabled(flag);
        setfdbs.setEnabled(flag);
        jtftotalfz.setEnabled(flag);
        settotalfz.setEnabled(flag);
        jtftotalfdbs.setEnabled(flag);
        settotalfdbs.setEnabled(flag);
    }


}
