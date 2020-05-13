package com.thingtek.view.shell.systemSetup.systemSetupComptents;

import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.config.PortConfig;
import com.thingtek.socket.CollectServer;
import com.thingtek.socket.CollectSocket;
import com.thingtek.socket.agreement.SocketAgreement;
import com.thingtek.socket.entity.BaseS2G;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.DisUnitAdminTableModel;
import com.thingtek.view.shell.DataPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class LXUnitAdminSetPanel extends BaseSystemPanel {

    @Resource
    private CollectServer server;
    @Resource
    private SocketAgreement agreement;

    private JTable onlytable;
    @Resource
    private DisUnitAdminTableModel tableModel;

    @Resource
    private PortConfig portConfig;

    @Override
    protected void initCenter() {
        super.initCenter();
        onlytable = new JTable();

        JPanel center = new JPanel(new BorderLayout());
        addCenter(center);
        JPanel centertitle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centertitle.setBackground(factorys.getColorFactory().getColor("centertitle"));

        onlytable.setModel(tableModel);

        JLabel jlmcip = new JLabel("脉冲IP:");
        centertitle.add(jlmcip);
        jtfMCIP = new JTextField();
        jtfMCIP.setPreferredSize(new Dimension(100, 20));
        jtfMCIP.setText(portConfig.getMcip());
        centertitle.add(jtfMCIP);
        JLabel jlmcport = new JLabel("脉冲端口:");
        centertitle.add(jlmcport);
        jtfMCPORT = new JTextField();
        jtfMCPORT.setPreferredSize(new Dimension(50, 20));
        jtfMCPORT.setText(String.valueOf(portConfig.getMcport()));
        centertitle.add(jtfMCPORT);
        JLabel jlmc = new JLabel("脉冲间隔:");
        centertitle.add(jlmc);
        jtfMC = new JTextField();
        jtfMC.setPreferredSize(new Dimension(50, 20));
        centertitle.add(jtfMC);
        EditButton setmc = new EditButton("设置脉冲间隔", factorys.getIconFactory().getIcon("set"));
        setmc.addActionListener(e -> {
            String strmc = jtfMC.getText();
            boolean flag = isInt(strmc);
            if (!flag) {
                errorMessage("脉冲输入有误!");
                return;
            }
            int mc = Integer.parseInt(strmc);
            try {
                if (checkmcip_port()) {
                    return;
                }
                CollectSocket socket = server.getSocket(getmcip(), getmcport());
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
        });
        centertitle.add(setmc);
        EditButton searchmc = new EditButton("查询脉冲间隔", factorys.getIconFactory().getIcon("search"));
        searchmc.addActionListener(e -> {
            try {
                if (checkmcip_port()) {
                    return;
                }
                CollectSocket socket = server.getSocket(getmcip(), getmcport());
                if (socket == null) {
                    falseMessage("脉冲板离线!");
                    return;
                }
                BaseS2G s2g = agreement.getS2G("searchmc");
                socket.sendMSG(s2g.getResult());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        centertitle.add(searchmc);
        center.add(centertitle, BorderLayout.NORTH);

        JPanel tablepanel = new JPanel(new BorderLayout());
        JScrollPane jspTable = new JScrollPane(onlytable);
        tablepanel.add(jspTable, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(cardLayout);
        centerPanel.add(tablepanel, "only");

        center.add(centerPanel, BorderLayout.CENTER);
        initializeTable();
    }

    private boolean checkmcip_port() {
        String mcip = jtfMCIP.getText();
        if (!isIp(mcip)) {
            errorMessage("脉冲IP输入有误!");
            return true;
        }
        String strmcport = jtfMCPORT.getText();
        if (!isNum(strmcport)) {
            errorMessage("脉冲端口输入有误!");
            return true;
        }
        int mcport = Integer.parseInt(strmcport);
        if (mcport < 5000 || mcport > 65535) {
            errorMessage("脉冲端口输入有误!");
            return true;
        }
        portConfig.setMcip(mcip);
        portConfig.setMcport(mcport);
        portConfig.refreshConfigXml();
        return false;
    }

    private String getmcip() {
        return jtfMCIP.getText();
    }

    private int getmcport() {
        String strmcport = jtfMCPORT.getText();
        return Integer.parseInt(strmcport);
    }

    private CardLayout cardLayout = new CardLayout();
    private boolean centerflag = false;
    private JTextField jtfMCIP;
    private JTextField jtfMCPORT;
    private EditButton totalset;
    private EditButton apply;
    private EditButton setfz;
    private JTextField jtftotalfz;
    private EditButton settotalfz;
    private JTextField jtfMC;
    private EditButton setfdbs;
    private JTextField jtftotalfdbs;
    private EditButton settotalfdbs;

    @Override
    protected void initToolbar() {
        super.initToolbar();
        EditButton refresh = addTool("刷新", "refresh");
        refresh.addActionListener(e -> refreshUnit());
        totalset = addTool("批量设置", "set");
        totalset.addActionListener(e -> {
            if (centerflag) {
                totalset.setText("批量设置");
                centerflag = false;
            } else {
                totalset.setText("单个设置");
                centerflag = true;
            }
            refreshVisible();
        });


        apply = addTool("保存", "apply");
        apply.addActionListener(e -> {
            stopEditing();
            if (!checkinput()) {
                refreshUnit();
                return;
            }
            List<LXUnitBean> units = new ArrayList<>();
            for (int i = 0; i < onlytable.getRowCount(); i++) {
                LXUnitBean unit = unitService.getUnitByNumber((Short) onlytable.getValueAt(i, 0));
                unit.resolveAdminTable(onlytable, i);
                units.add(unit);
            }
            if (unitService.updateLXUnit(units.toArray(new LXUnitBean[0]))) {
                successMessage("保存成功");
                refreshUnit();
            } else {
                falseMessage("保存失败");
            }
        });

        setfz = addTool("设置阈值", "set");
        setfz.addActionListener(e -> {

            stopEditing();
            int row = onlytable.getSelectedRow();
            int[] rows = onlytable.getSelectedRows();
            if (rows.length <= 0) {
                errorMessage("请先选择单元!");
                return;
            }
            LXUnitBean unit = unitService.getUnitByNumber((Short) onlytable.getValueAt(row, 0));
            if (!checkInput(row)) {
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
            unitService.updateLXUnit(unit);
        });

        setfdbs = addTool("设置放大倍数", "set");
        setfdbs.addActionListener(e -> {

            stopEditing();
            int row = onlytable.getSelectedRow();
            int[] rows = onlytable.getSelectedRows();
            if (rows.length <= 0) {
                errorMessage("请先选择单元!");
                return;
            }
            LXUnitBean unit = unitService.getUnitByNumber((Short) onlytable.getValueAt(row, 0));
            if (!checkInput(row)) {
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
            bytes[0] = (byte) (w > 127 ? 127 : w < 13 ? 13 : w);
            s2g.setDatas(bytes);
            try {
                socket.sendMSG(s2g.getResult());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            unitService.updateLXUnit(unit);
        });
        jtftotalfz = new JTextField();
        jtftotalfz.setPreferredSize(new Dimension(40, 20));
        jtftotalfz.setVisible(false);
        addTool(jtftotalfz);
        settotalfz = addTool("设置阈值", "set");
        settotalfz.setVisible(false);
        settotalfz.addActionListener(e -> {
            String strfz = jtftotalfz.getText();
            if (checkfz(strfz)) {
                errorMessage("阈值输入有误!(2000-4095)");
                return;
            }
            int fz = Integer.parseInt(strfz);
            List<LXUnitBean> units = unitService.getAll();
            for (LXUnitBean baseunit : units) {
                CollectSocket socket = server.getSocket(baseunit.getIp(), baseunit.getPort());
                if (socket == null) {
                    continue;
                }
                BaseS2G s2g = agreement.getS2G("setfz");
                s2g.setUnitnum(baseunit.getUnit_num());
                byte[] bytes = new byte[2];
                bytes[0] = (byte) (fz & 0xff);
                bytes[1] = (byte) ((fz >> 8) & 0xff);
                s2g.setDatas(bytes);
                baseunit.setFz(fz);
                try {
                    socket.sendMSG(s2g.getResult());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            unitService.updateLXUnit(units.toArray(new LXUnitBean[0]));
            refreshUnit();
        });
        jtftotalfdbs = new JTextField();
        jtftotalfdbs.setPreferredSize(new Dimension(40, 20));
        jtftotalfdbs.setVisible(false);
        addTool(jtftotalfdbs);
        settotalfdbs = addTool("设置放大倍数", "set");
        settotalfdbs.setVisible(false);
        settotalfdbs.addActionListener(e -> {
            String strfdbs = jtftotalfdbs.getText();
            if (checkfdbs(strfdbs)) {
                errorMessage("放大倍数输入有误!(13-127)");
                return;
            }
            int fdbs = Integer.parseInt(strfdbs);
            List<LXUnitBean> units = unitService.getAll();
            for (LXUnitBean baseunit : units) {
                CollectSocket socket = server.getSocket(baseunit.getIp(), baseunit.getPort());
                if (socket == null) {
                    continue;
                }
                BaseS2G s2g = agreement.getS2G("setfdbs");
                s2g.setUnitnum(baseunit.getUnit_num());
                byte[] bytes = new byte[2];
                bytes[0] = (byte) (fdbs & 0xff);
                bytes[1] = (byte) ((fdbs >> 8) & 0xff);
                s2g.setDatas(bytes);

                baseunit.setFdbs(fdbs);
                try {
                    socket.sendMSG(s2g.getResult());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            unitService.updateLXUnit(units.toArray(new LXUnitBean[0]));
            refreshUnit();
        });
    }

    @Override
    public void loadingData() {
        refreshUnit();
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < onlytable.getRowCount(); i++) {
                    short unit_num = (short) onlytable.getValueAt(i, 0);
                    LXUnitBean unitBean = unitService.getUnitByNumber(unit_num);
                    onlytable.setValueAt(unitBean.isConnect(), i, 7);
                }
            }
        }, 10000, 5000);
    }

    public void refreshUnit() {
        List<LXUnitBean> units = unitService.getAll();
        List<Vector<Object>> vectors = new ArrayList<>();
        for (LXUnitBean unit : units) {
            Vector<Object> vector = unit.getAdminSetTableCol();
            vectors.add(vector);
        }
        tableModel.addDatas(vectors);
        for (DataPanel dataPanel : logoInfo.getDataPanels()) {
            dataPanel.refreashData();
        }
    }

    private void stopEditing() {
        if (onlytable.isEditing())
            onlytable.getCellEditor().stopCellEditing();
    }

    private boolean checkfz(String fz) {
        if (fz == null || fz.equals("") || !isInt(fz)) {
            return true;
        }
        int fzint = Integer.parseInt(fz);
        return fzint < 2000 || fzint > 4095;
    }

    private boolean checkfdbs(String fdbs) {
        if (fdbs == null || fdbs.equals("") || !isInt(fdbs)) {
            return true;
        }
        int fzint = Integer.parseInt(fdbs);
        return fzint < 13 || fzint > 127;
    }

    private boolean checkInput(int row) {
        short unit_num = (short) onlytable.getValueAt(row, 0);
        Object objfz = onlytable.getValueAt(row, 1);
        String fz = objfz == null ? null : String.valueOf(objfz);
        if (checkfz(fz)) {
            errorMessage("单元编号 " + unit_num + " 阈值输入有误!(2000-4095)");
            return false;
        }
        Object objfdbs = onlytable.getValueAt(row, 2);
        String fdbs = objfdbs == null ? null : String.valueOf(objfdbs);
        if (checkfdbs(fdbs)) {
            errorMessage("单元编号 " + unit_num + " 放大倍数输入有误!(13-127)");
            return false;
        }
        Object objip = onlytable.getValueAt(row, 3);
        String ip = objip == null ? null : String.valueOf(objip);
        if (!(ip == null || ip.trim().equals("") || isIp(ip))) {
            errorMessage("单元编号 " + unit_num + " IP地址输入有误");
            return false;
        }
        Object objport = onlytable.getValueAt(row, 4);
        String port = objport == null ? null : String.valueOf(objport);
        if (!(port == null || isNum(port))) {
            errorMessage("单元编号 " + unit_num + " IP地址输入有误");
            return false;
        }
        Object objplace_value = onlytable.getValueAt(row, 5);
        String place_value = objplace_value == null ? null : String.valueOf(objplace_value);
        if (!(place_value == null || isNum(place_value))) {
            errorMessage("单元编号 " + unit_num + " 安装位置输入有误");
            return false;
        }
        Object objpoint = onlytable.getValueAt(row, 6);
        String point = objpoint == null ? null : String.valueOf(objpoint);
        if (!(point == null || isNum(point))) {
            errorMessage("单元编号 " + unit_num + " 安装位置输入有误");
            return false;
        }
        return true;
    }

    private boolean checkinput() {
        for (int i = 0; i < onlytable.getRowCount(); i++) {
            boolean flag = checkInput(i);
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    @Resource
    private TCR tcr;

    private void initializeTable() {
        tcr.initializeTable(onlytable);
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

}
