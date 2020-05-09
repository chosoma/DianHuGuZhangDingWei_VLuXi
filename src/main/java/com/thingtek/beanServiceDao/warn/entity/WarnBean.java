package com.thingtek.beanServiceDao.warn.entity;

import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import lombok.Data;

import javax.swing.*;
import java.util.Date;
import java.util.Vector;

@Data
public class WarnBean {

    private int id;
    private String warn_info;
    private String pipe_name;
    private PipeBean pipe;
    private Date inserttime;

    public void resolveTotalInfoTable(JTable table, int row) {
        id = (int) table.getValueAt(row, 0);
        pipe_name = (String) table.getValueAt(row, 1);
        warn_info = (String) table.getValueAt(row, 2);
        inserttime = (Date) table.getValueAt(row, 3);
    }

    public Vector<Object> getTableCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(id);
        vector.add(pipe_name);
        vector.add(warn_info);
        vector.add(inserttime);
        return vector;
    }

    public Vector<Object> getCollectTableCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(pipe_name);
        vector.add(warn_info);
        vector.add(inserttime);
        return vector;
    }

}
