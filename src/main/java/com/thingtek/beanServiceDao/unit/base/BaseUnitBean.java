package com.thingtek.beanServiceDao.unit.base;

import com.thingtek.beanServiceDao.clt.entity.CltBean;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import lombok.Data;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Data
public abstract class BaseUnitBean {
    protected Short unit_num;//单元编号
    protected PointBean point;//测点
    protected Integer point_num;
    protected String phase;
    protected Map<String, Object> one;
    protected CltBean clt;
    protected String data_table_name;

    protected BaseUnitBean() {
        one = new HashMap<>();
    }

    public void resolve(Map<String, Object> one) {
        this.one = one;
        unit_num = (short) (int) (Integer) one.get("UNIT_NUM");
        point_num = (Integer) one.get("POINT_NUM");
        phase = one.get("PHASE") == null ? "" : (String) one.get("PHASE");
        data_table_name = one.get("DATA_TABLE_NAME") == null ? null : (String) one.get("DATA_TABLE_NAME");
    }

    public void resolve2map() {
        one.put("UNIT_NUM", unit_num);
        one.put("POINT_NUM", point_num);
        one.put("PHASE", phase);
    }

    public void resolveTable(JTable table, int row) {
        phase = (String) table.getValueAt(row, 2);
        one.put("PHASE", phase);
    }

    public abstract void resolveInputTable(JTable table, int row);

    public Vector<Object> getTableCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(unit_num);
        vector.add(point.getPoint_name());
        vector.add(phase);
        return vector;
    }

    public abstract Vector<Object> getSetTableCol();

    public abstract Vector<Object> getSetTotalTableCol();

    public Object get(String key) {
        return one.get(key);
    }

}
