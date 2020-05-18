package com.thingtek.beanServiceDao.warn.entity;

import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import lombok.Data;

import javax.swing.*;
import java.util.Date;
import java.util.Vector;

@Data
public class WarnBean {

    private int id;
    private LXUnitBean nearunit;
    private Short near_unit_num;
    private LXUnitBean tounit;
    private Short to_unit_num;
    private double weizhi;
    private String warn_info;
    private String pipe_name;
    private PipeBean pipe;
    private int pipe_id;
    private Date inserttime;
    private double place_value;

    public void setPipe(PipeBean pipe) {
        this.pipe = pipe;
        this.pipe_id = pipe.getPipe_id();
        this.pipe_name = pipe.getPipe_name();
    }

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
        vector.add(getWarnInfo());
        vector.add(inserttime);
        return vector;
    }

    public Vector<Object> getCollectTableCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(pipe_name);
        vector.add(getWarnInfo());
        vector.add(inserttime);
        return vector;
    }

    private String getWarnInfo() {
        String warn_info;
        if (tounit != null && nearunit != null) {
            warn_info = "自\"" + nearunit.getPlace_name() + "\"向\"" + tounit.getPlace_name() + "\" " + place_value + "米";
        } else {
            if (nearunit != null) {
                warn_info = "\"" + nearunit.getPlace_name() + "\"附近 " + place_value + "米";
            } else {
                warn_info = place_value + "米";
            }
        }
        return warn_info;
    }

}
