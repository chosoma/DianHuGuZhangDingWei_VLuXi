package com.thingtek.beanServiceDao.point.entity;

import com.thingtek.beanServiceDao.clt.entity.CltBean;
import lombok.Data;

import javax.swing.*;
import java.util.Vector;

public @Data
class PointBean {

    private Integer point_num;
    private String point_name;
    private double x;
    private double y;
    private CltBean clt;
    private int clt_type;
    private String clt_name;
    private int place_value;

    public void resolveTable(JTable table, int row) {
        point_name = (String) table.getValueAt(row, 2);
        place_value = (int) table.getValueAt(row, 3);
    }

    public Vector<Object> getTableCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(clt_name);
        vector.add(point_num);
        vector.add(point_name);
        vector.add(place_value);
        return vector;
    }

    public void setClt(CltBean clt) {
        this.clt = clt;
        if (clt != null) {
            this.clt_name = clt.getType_name();
        }
    }

}
