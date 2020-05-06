package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Vector;

@Component
public class DisDataTableModel extends BaseTableModel {
    public DisDataTableModel() {
        super();
        typeArray = new Class[]{
                Short.class,
                String.class,
                String.class,
//                Integer.class,
//                Long.class,
                Date.class
        };
        columnWidthes = new int[]{
                70, 70, 45, 145
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
        column.add("单元编号");
        column.add("位置名称");
        column.add("相位");
//        column.add("信号强度");
        column.add("时间");
//        column.add("触发时间");
        this.setDataVector(row, column);
    }
}
