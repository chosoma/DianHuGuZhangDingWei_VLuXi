package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Vector;

@Component
public class WarnTableModel extends BaseTableModel {
    public WarnTableModel() {
        super();
        typeArray = new Class[]{
                Integer.class,
                String.class,
                String.class,
//                String.class,
                Date.class
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<>();
        column.add("id");
        column.add("异常位置");
        column.add("异常相位");
//        column.add("报警原因");
        column.add("异常发生时间");
        this.setDataVector(row, column);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
