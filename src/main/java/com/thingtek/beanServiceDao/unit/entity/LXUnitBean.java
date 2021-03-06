package com.thingtek.beanServiceDao.unit.entity;

import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import lombok.Data;

import javax.swing.*;
import java.util.Vector;

@Data
public class LXUnitBean {

    protected Short unit_num;//单元编号
    protected String data_table_name;
    protected PipeBean pipe;
    protected int pipe_id;
    private Integer fz;
    private Integer fdbs;
    private String ip;
    private Integer port;
    private double x;
    private double y;
    private Integer pipe_page;
    private String place_name;
    private Integer place_value;
    private Integer point;//点位
    private boolean connect;
    private short addr;


    public void resolveLXTable(JTable table, int row) {
        int pipe_page = (Integer) table.getValueAt(row, 2);
        setPipe_page(pipe_page);
        if (pipe != null) {
            setPipe_id(pipe.getPipe_id());
        }
        place_name = String.valueOf(table.getValueAt(row, 3));
    }

    public void resolveAdminTable(JTable table, int row) {
        String strfz = String.valueOf(table.getValueAt(row, 1));
        fz = strfz.equals("null") || strfz.equals("") ? 2500 : Integer.parseInt(strfz);
        String strfdbs = String.valueOf(table.getValueAt(row, 2));
        fdbs = strfdbs.equals("null") || strfdbs.equals("") ? 20 : Integer.parseInt(strfdbs);
        ip = (String) table.getValueAt(row, 3);
        String stport = String.valueOf(table.getValueAt(row, 4));
        port = stport.equals("null") || stport.equals("") ? 1234 : Integer.parseInt(stport);
        String strplacevalue = String.valueOf(table.getValueAt(row, 5));
        place_value = strplacevalue.equals("null") || strplacevalue.equals("") ? 1 : Integer.parseInt(strplacevalue);
        String strpoint = String.valueOf(table.getValueAt(row, 6));
        point = strpoint.equals("null") || strpoint.equals("") ? 1 : Integer.parseInt(strpoint);
    }

    public Vector<Object> getLXTableCol() {

        Vector<Object> vector = new Vector<>();
        vector.add(unit_num);
        vector.add(pipe.getPipe_name());
        vector.add(pipe_page);
        vector.add(place_name);
        return vector;
    }

    public Vector<Object> getAdminSetTableCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(unit_num);
        vector.add(fz);
        vector.add(fdbs);
        vector.add(ip);
        vector.add(port);
        vector.add(place_value);
        vector.add(point);
        vector.add(connect);
        vector.add(pipe.getPipe_name());
        vector.add(pipe_page);
        vector.add(place_name);
        return vector;
    }

}
