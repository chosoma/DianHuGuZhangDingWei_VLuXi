package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

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
                String.class,
                Integer.class,
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
        column.add("安装位置");
        this.setDataVector(row, column);
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0;
    }
}
