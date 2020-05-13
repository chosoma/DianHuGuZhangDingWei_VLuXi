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
                String.class,
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<>();
        column.add("单元编号");
        column.add("所属管体");
        column.add("所属段");
        column.add("位置名");
        this.setDataVector(row, column);
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0;
    }
}
