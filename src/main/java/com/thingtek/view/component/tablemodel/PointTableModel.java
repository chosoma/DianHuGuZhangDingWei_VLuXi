package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Vector;

@Component
public class PointTableModel extends BaseTableModel {
    public PointTableModel() {
        super();
        typeArray = new Class[]{
                JComboBox.class,
                Integer.class,
                String.class,
                Integer.class
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
        column.add("测点类型");
        column.add("测点编号");
        column.add("测点名称");
        column.add("安装位置(米)");
        this.setDataVector(row, column);
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 1;
    }

}
