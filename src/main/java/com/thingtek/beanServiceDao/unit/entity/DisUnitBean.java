package com.thingtek.beanServiceDao.unit.entity;

import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
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
    private float x;
    private float y;
    private Integer pipe_page;
    private String place_name;
    private Integer place_value;

    public void resolve2map() {
        super.resolve2map();
        one.put("FZ", fz);
        one.put("FDBS", fdbs);
        one.put("IP", ip);
        one.put("PORT", port);
        one.put("PLACE_VALUE", place_value);
    }

    public void resolve(Map<String, Object> one) {
        super.resolve(one);
        fz = (Integer) (one.get("FZ") == null ? 2500 : one.get("FZ"));
        fdbs = (Integer) (one.get("FDBS") == null ? 15 : one.get("FDBS"));
        ip = (String) one.get("IP");
        port = (Integer) (one.get("PORT") == null ? 8888 : one.get("PORT"));
        x = one.get("X") == null ? 0 : (float) one.get("X");
        y = one.get("Y") == null ? 0 : (float) one.get("Y");
        pipe_id = one.get("PIPE_ID") == null ? 1 : (int) one.get("PIPE_ID");
        pipe_page = one.get("PIPE_PAGE") == null ? 1 : (int) one.get("PIPE_PAGE");
        place_name = (String) one.get("PLACE_NAME");
        place_value = one.get("PLACE_VALUE") == null ? 0 : (int) one.get("PLACE_VALUE");
    }

    public void resolveTable(JTable table, int row) {
        super.resolveTable(table, row);
        String strpipepage = String.valueOf(table.getValueAt(row, 2));
        pipe_page = strpipepage.equals("") ? null : Integer.parseInt(strpipepage);
        one.put("PIPE_PAGE", pipe_page);
    }

    public void resolveLXTable(JTable table, int row) {
        super.resolveLXTable(table, row);
    }

    @Override
    public void resolveAdminTable(JTable table, int row) {
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
        String strplacevalue = String.valueOf(table.getValueAt(row, 5));
        place_value = strplacevalue.equals("") ? null : Integer.parseInt(strplacevalue);
        one.put("PLACE_VALUE", place_value);

    }

    @Override
    public Vector<Object> getLXTableCol() {
        Vector<Object> vector = super.getLXTableCol();
        vector.add(pipe.getPipe_name());
        vector.add(pipe_page);
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
        return vector;
    }

    @Override
    public Vector<Object> getAdminSetTotalTableCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(unit_num);
        return vector;
    }

}
