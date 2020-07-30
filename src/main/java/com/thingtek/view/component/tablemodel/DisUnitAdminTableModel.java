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
                String.class,
                String.class,
                String.class,
        };
        columnWidthes = new int[]{
                30, 30, 30, 145,
                30, 80, 30, 30,
                30, 30, 30
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
        column.add("所属管体");
        column.add("所属段");
        column.add("位置名");
        this.setDataVector(row, column);
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        return col > 0 && col < 7;
    }
}
