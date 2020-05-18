package com.thingtek.view.shell.systemSetup;

import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.config.PortConfig;
import com.thingtek.socket.CollectServer;
import com.thingtek.socket.CollectSocket;
import com.thingtek.socket.agreement.SocketAgreement;
import com.thingtek.socket.entity.BaseS2G;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.DisUnitAdminTableModel;
import com.thingtek.view.shell.base.DataPanel;
import com.thingtek.view.shell.base.BaseSystemPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class LXUnitAdminSetPanel extends BaseSystemPanel {

    @Resource
    private CollectServer server;
    @Resource
    private SocketAgreement agreement;

    private JTable table;
    @Resource
    private DisUnitAdminTableModel tableModel;

    @Resource
    private PortConfig portConfig;

    @Override
    protected void initCenter() {
        super.initCenter();
        table = new JTable();

        JPanel center = new JPanel(new BorderLayout());
        addCenter(center);
        JPanel centertitle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centertitle.setBackground(factorys.getColorFactory().getColor("centertitle"));

        table.setModel(tableModel);

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
        JScrollPane jspTable = new JScrollPane(table);
        tablepanel.add(jspTable, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(tablepanel, BorderLayout.CENTER);

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
                unit.resolveAdminTable(table, i);
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
        setfz.addActionListener(e -> setone("setfz"));

        setfdbs = addTool("设置放大倍数", "set");
        setfdbs.addActionListener(e -> setone("setfdbs"));

        jtftotalfz = new JTextField();
        jtftotalfz.setPreferredSize(new Dimension(40, 20));
        jtftotalfz.setVisible(false);
        addTool(jtftotalfz);

        settotalfz = addTool("设置阈值", "set");
        settotalfz.setVisible(false);
        settotalfz.addActionListener(e -> setall("setfz"));

        jtftotalfdbs = new JTextField();
        jtftotalfdbs.setPreferredSize(new Dimension(40, 20));
        jtftotalfdbs.setVisible(false);
        addTool(jtftotalfdbs);

        settotalfdbs = addTool("设置放大倍数", "set");
        settotalfdbs.setVisible(false);
        settotalfdbs.addActionListener(e -> setall("setfdbs"));
        if (!logoInfo.isAdmin()) {
            visibleall();
        }
    }

    private void setone(String keycmd) {
        stopEditing();
        int row = table.getSelectedRow();
        int[] rows = table.getSelectedRows();
        if (rows.length <= 0) {
            errorMessage("请先选择单元!");
            return;
        }
        LXUnitBean unit = unitService.getUnitByNumber((Short) table.getValueAt(row, 0));
        if (!checkInput(row)) {
            refreshUnit();
            return;
        }
        unit.resolveAdminTable(table, row);
        CollectSocket socket = server.getSocket(unit.getIp(), unit.getPort());
        if (socket == null) {
            falseMessage("单元未连接!");
            return;
        }
        BaseS2G s2g = agreement.getS2G(keycmd);
        s2g.setUnitnum(unit.getUnit_num());
        int datalength = 0;
        int value = 0;
        switch (keycmd) {
            case "setfdbs":
                datalength = 1;
                value = unit.getFdbs();
                break;
            case "setfz":
                datalength = 2;
                value = unit.getFz();
                break;
        }
        byte[] bytes = new byte[datalength];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (value >> (i * 8));
        }
        s2g.setDatas(bytes);
        try {
            socket.sendMSG(s2g.getResult());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        unitService.updateLXUnit(unit);
    }

    private void setall(String keycmd) {
        String strvalue = "0";
        switch (keycmd) {
            case "setfdbs":
                strvalue = jtftotalfdbs.getText();
                if (checkfdbs(strvalue)) {
                    errorMessage("放大倍数输入有误!(13-127)");
                    return;
                }
                break;
            case "setfz":
                strvalue = jtftotalfz.getText();
                if (checkfz(strvalue)) {
                    errorMessage("阈值输入有误!(2000-4095)");
                    return;
                }
                break;
        }

        int value = Integer.parseInt(strvalue);
        List<LXUnitBean> units = unitService.getAll();
        for (LXUnitBean unit : units) {
            CollectSocket socket = server.getSocket(unit.getIp(), unit.getPort());
            if (socket == null) {
                continue;
            }
            BaseS2G s2g = getS2G(keycmd, unit, value);
            switch (keycmd) {
                case "setfdbs":
                    unit.setFdbs(value);
                    break;
                case "setfz":
                    unit.setFz(value);
                    break;
            }
            try {
                socket.sendMSG(s2g.getResult());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        unitService.updateLXUnit(units.toArray(new LXUnitBean[0]));
        refreshUnit();
    }

    private BaseS2G getS2G(String keycmd, LXUnitBean unit, int data_int) {
        BaseS2G s2g = agreement.getS2G(keycmd);
        s2g.setUnitnum(unit.getUnit_num());
        int length = 0;
        switch (keycmd) {
            case "setfdbs":
                length = 1;
                break;
            case "setfz":
                length = 2;
                break;
        }
        byte[] bytes = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (data_int >> (i * 8));
        }
        s2g.setDatas(bytes);
        return s2g;
    }

    private void visibleall() {
        apply.setVisible(false);
        setfz.setVisible(false);
        setfdbs.setVisible(false);
        jtftotalfz.setVisible(false);
        settotalfz.setVisible(false);
        jtftotalfdbs.setVisible(false);
        settotalfdbs.setVisible(false);
        totalset.setVisible(false);
    }

    @Override
    public void loadingData() {
        refreshUnit();
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < table.getRowCount(); i++) {
                    short unit_num = (short) table.getValueAt(i, 0);
                    LXUnitBean unitBean = unitService.getUnitByNumber(unit_num);
                    if (unitBean != null) {
                        table.setValueAt(unitBean.isConnect(), i, 7);
                    }
                }
            }
        }, 10000, 5000);
    }

    @Override
    public void refreshTable() {
        refreshUnit();
    }

    private void refreshUnit() {
        List<LXUnitBean> units = unitService.getAll();
        List<Vector<Object>> vectors = new ArrayList<>();
        for (LXUnitBean unit : units) {
            Vector<Object> vector = unit.getAdminSetTableCol();
            vectors.add(vector);
        }
        tableModel.addDatas(vectors);
    }

    private void stopEditing() {
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
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
        short unit_num = (short) table.getValueAt(row, 0);
        Object objfz = table.getValueAt(row, 1);
        String fz = objfz == null ? null : String.valueOf(objfz);
        if (checkfz(fz)) {
            errorMessage("单元编号 " + unit_num + " 阈值输入有误!(2000-4095)");
            return false;
        }
        Object objfdbs = table.getValueAt(row, 2);
        String fdbs = objfdbs == null ? null : String.valueOf(objfdbs);
        if (checkfdbs(fdbs)) {
            errorMessage("单元编号 " + unit_num + " 放大倍数输入有误!(13-127)");
            return false;
        }
        Object objip = table.getValueAt(row, 3);
        String ip = objip == null ? null : String.valueOf(objip);
        if (!(ip == null || ip.trim().equals("") || isIp(ip))) {
            errorMessage("单元编号 " + unit_num + " IP地址输入有误");
            return false;
        }
        Object objport = table.getValueAt(row, 4);
        String port = objport == null ? null : String.valueOf(objport);
        if (!(port == null || port.trim().equals("") || isNum(port))) {
            errorMessage("单元编号 " + unit_num + " 端口号输入有误");
            return false;
        }
        Object objplace_value = table.getValueAt(row, 5);
        String place_value = objplace_value == null ? null : String.valueOf(objplace_value);
        if (!(place_value == null || place_value.trim().equals("") || isNum(place_value))) {
            errorMessage("单元编号 " + unit_num + " 安装位置输入有误");
            return false;
        }
        Object objpoint = table.getValueAt(row, 6);
        String point = objpoint == null ? null : String.valueOf(objpoint);
        if (!(point == null || point.trim().equals("") || isNum(point))) {
            errorMessage("单元编号 " + unit_num + " 点位输入有误");
            return false;
        }
        return true;
    }

    private boolean checkinput() {
        for (int i = 0; i < table.getRowCount(); i++) {
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
        tcr.initializeTable(table);
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
