package com.thingtek.view.component.tablemodel;

import org.springframework.stereotype.Component;

import java.util.Vector;

/*
管体管理表模型
 */
@Component
public class PipeTableModel extends BaseTableModel {
    public PipeTableModel() {
        super();
        typeArray = new Class[]{
                Integer.class,
                String.class,
                Integer.class
        };
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<>();
        column.add("管体编号");
        column.add("管体名称");
        column.add("管体分段");
        this.setDataVector(row, column);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0;
    }
}
