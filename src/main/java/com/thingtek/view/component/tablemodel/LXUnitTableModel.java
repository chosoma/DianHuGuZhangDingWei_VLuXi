package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Vector;

@Component
public class LXUnitTableModel extends BaseTableModel {
    public LXUnitTableModel() {
        super();
        typeArray = new Class[]{
                Short.class,
                JComboBox.class,
                JComboBox.class,
//                Integer.class
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
        column.add("单元编号");
        column.add("所属管体");
        column.add("管体段位置");
//        column.add("安装位置");
        this.setDataVector(row, column);
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0;
    }
}
