package com.thingtek.beanServiceDao.warn.entity;

import lombok.Data;

import javax.swing.*;
import java.util.Date;
import java.util.Vector;

@Data
public class WarnBean {

    private int clt_type = 4;
    private int id;
    private String warn_info;
    private String phase;
    private Date inserttime;

    public void resolveTotalInfoTable(JTable table, int row) {
        id = (int) table.getValueAt(row, 0);
        warn_info = (String) table.getValueAt(row, 1);
        phase = (String) table.getValueAt(row, 2);
        inserttime = (Date) table.getValueAt(row, 3);
    }

    public Vector<Object> getTableCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(id);
        vector.add(warn_info);
        vector.add(phase);
        vector.add(inserttime);
        return vector;
    }

    public Vector<Object> getCollectTableCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(warn_info);
        vector.add(phase);
        vector.add(inserttime);
        return vector;
    }

}
