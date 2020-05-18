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
                Integer.class,
                Date.class
        };
        columnWidthes = new int[]{
                70, 70, 45, 145
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<>();
        column.add("单元编号");
        column.add("所属管体");
        column.add("管体段");
        column.add("时间");
        this.setDataVector(row, column);
    }
}
