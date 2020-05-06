package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Vector;

@Component
public class DisUnitTableModel extends BaseTableModel {
    public DisUnitTableModel() {
        super();
        typeArray = new Class[]{
                Short.class,
//                String.class,
                JComboBox.class,
                JComboBox.class,
                String.class,
                String.class,
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
        column.add("单元编号");
//        column.add("单元名称");
        column.add("位置名称");
        column.add("相位");
//        column.add("阈值");
//        column.add("IP地址");
        this.setDataVector(row, column);
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0;
    }
}
