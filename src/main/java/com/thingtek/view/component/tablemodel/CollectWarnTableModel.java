package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Date;
import java.util.Vector;

@Component
public class CollectWarnTableModel extends BaseTableModel {
    public CollectWarnTableModel() {
        super();
        typeArray = new Class[]{
                String.class,
                String.class,
//                String.class,
                Date.class,
//                JButton.class
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
        column.add("异常位置");
        column.add("异常相位");
//        column.add("报警原因");
        column.add("异常发生时间");
//        column.add("");
        this.setDataVector(row, column);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 4;
    }
}
