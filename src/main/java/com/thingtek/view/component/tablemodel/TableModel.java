package com.thingtek.view.component.tablemodel;

import java.util.Date;
import java.util.Vector;

public class TableModel extends BaseTableModel {

    public TableModel() {
        super();
        typeArray = new Class[]{
                String.class,
                String.class,
                String.class,
                String.class
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
        column.add("1");
        column.add("2");
        column.add("3");
        column.add("4");
        this.setDataVector(row, column);
    }

}
