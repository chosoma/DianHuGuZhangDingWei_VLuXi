package com.thingtek.beanServiceDao.data.base;

import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import lombok.Data;

import javax.swing.*;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

@Data
public abstract class BaseDataBean {

    protected BaseUnitBean unit;
    protected Short unit_num;
    protected Date inserttime;
    protected Map<String, Object> one;


    public void resolve(Map<String, Object> one) {
        this.one = one;
        unit_num = (short) (int) (Integer) one.get("UNIT_NUM");
        inserttime = (Date) one.get("INSERTTIME");
    }

    public void resolveTotalInfoTable(JTable table, int selectrow) {
        this.unit_num = (Short) table.getValueAt(selectrow, 0);
        String pointname = (String) table.getValueAt(selectrow, 1);
        String phase = (String) table.getValueAt(selectrow, 2);
    }

    protected int bytes2int(int off, int length, byte[] bytes) {
        int i = 0;
        for (int j = 0; j < length; j++) {
            i |= (bytes[off + j] & 0xff) << (j * 8);
        }
        return i;
    }

    public float newScale(float f1, float f2, int scale) {
        float f3 = f1 - f2;
        int i = (int) Math.round(f3 * Math.pow(10, scale));
        return (float) (i / Math.pow(10, scale));
    }

    public Vector<Object> getDataTotalCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(unit_num);
        if (unit != null && unit.getPoint() != null) {
            vector.add(unit.getPoint().getPoint_name());
        } else {
            vector.add(null);
        }
        if (unit != null) {
            vector.add(unit.getPhase());
        } else {
            vector.add(null);
        }
        return vector;
    }
}
