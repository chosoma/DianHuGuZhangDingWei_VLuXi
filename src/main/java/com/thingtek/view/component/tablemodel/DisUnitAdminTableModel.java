package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Vector;

@Component
public class DisUnitAdminTableModel extends BaseTableModel {
    public DisUnitAdminTableModel() {
        super();
        typeArray = new Class[]{
                Short.class,
                Integer.class,
                Integer.class,
                String.class,
                Integer.class,
                Integer.class,
                Integer.class,
                JTextField.class,
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<>();
        column.add("单元编号");
        column.add("阈值");
        column.add("放大倍数");
        column.add("IP地址");
        column.add("端口号");
        column.add("安装位置(米)");
        column.add("点位");
        column.add("在线状态");
        this.setDataVector(row, column);
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        return col > 0 && col < 7;
    }
}
