package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

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
        Vector<String> column = new Vector<>();
        column.add("所属管体");
        column.add("异常信息");
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
