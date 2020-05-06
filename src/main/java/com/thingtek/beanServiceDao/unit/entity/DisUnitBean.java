package com.thingtek.beanServiceDao.unit.entity;

import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.util.Map;
import java.util.Vector;

@EqualsAndHashCode(callSuper = true)
@Data
public class DisUnitBean extends BaseUnitBean {

    private Integer fz;
    private Integer fdbs;
    private String ip;
    private int port;

    public void resolve2map() {
        super.resolve2map();
        one.put("FZ", fz);
        one.put("FDBS", fdbs);
        one.put("IP", ip);
        one.put("PORT", port);
    }

    public void resolveTable(JTable table, int row) {
        super.resolveTable(table, row);
    }

    @Override
    public void resolveInputTable(JTable table, int row) {
        String strfz = String.valueOf(table.getValueAt(row, 1));
        fz = strfz.equals("") ? null : Integer.parseInt(strfz);
        one.put("FZ", fz);
        String strfdbs = String.valueOf(table.getValueAt(row, 2));
        fdbs = strfdbs.equals("") ? null : Integer.parseInt(strfdbs);
        one.put("FDBS", fdbs);
        ip = (String) table.getValueAt(row, 3);
        one.put("IP", ip);
        String stport = String.valueOf(table.getValueAt(row, 4));
        port = stport.equals("") ? 8888 : Integer.parseInt(stport);
        one.put("PORT", port);
    }

    public void resolve(Map<String, Object> one) {
        super.resolve(one);
        fz = (Integer) (one.get("FZ") == null ? 2500 : one.get("FZ"));
        fdbs = (Integer) (one.get("FDBS") == null ? 15 : one.get("FDBS"));
        ip = (String) one.get("IP");
        port = (Integer) (one.get("PORT") == null ? 8888 : one.get("PORT"));
    }

    public Vector<Object> getSetTableCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(unit_num);
        vector.add(fz);
        vector.add(fdbs);
        vector.add(ip);
        vector.add(port);
        return vector;
    }

    @Override
    public Vector<Object> getSetTotalTableCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(unit_num);
        return vector;
    }

}
