package com.thingtek.beanServiceDao.data.entity;

import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import lombok.Data;

import javax.swing.*;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

/**
 *
 */
@Data
public class DisDataBean implements Comparable<DisDataBean> {
    protected LXUnitBean unit;
    protected Short unit_num;
    protected Date inserttime;
    protected Map<String, Object> one;
    private int[] data;
    private int datacount;
    private int xhqd;
    private int gatewayfrontindex;
    private int serverindex;
    private int minindex;
    private long sj;
    private String datastring;

    public void setDatastring(String datastring) {
        this.datastring = datastring;
        data = new int[datastring.length()];
        for (int i = 0; i < data.length; i++) {
            data[i] = datastring.charAt(i);
        }
    }
    //初始版本
    /*public void setDatastring(String datastring) {
        this.datastring = datastring;
        String[] strs = datastring.split(",");
//            System.out.println(strs.length);
        data = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            data[i] = Integer.parseInt(strs[i]);
        }
        this.datastring = change();
    }*/

    public void resolve(byte[] datas) {
        this.data = resolveData2Line(datas);
    }

    public void resolveTotalInfoTable(JTable table, int selectrow) {
        this.unit_num = (Short) table.getValueAt(selectrow, 0);
        this.inserttime = (Date) table.getValueAt(selectrow, 3);
    }

    /*protected float bytes2float(byte[] b, int off, int length, int scale) {
        int temp = 0;
        for (int i = off, index = 0; i < off + length; i++, index++) {
            temp |= (b[i] & 0xFF) << (8 * index);
        }
        Float f = Float.intBitsToFloat(temp);
        if (Float.isNaN(f)) {
            return 0f;
        }
        BigDecimal bd = new BigDecimal(f);
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }*/

    private int[] resolveData2Line(byte[] datas) {
        int[] dataint = new int[datas.length / 2];
        for (int i = 0; i < dataint.length; i++) {
            dataint[i] = bytes2int(i * 2, 2, datas);
        }
        return dataint;
    }

    public Vector<Object> getDataTotalCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(unit_num);
        vector.add(unit.getPipe().getPipe_name());
        vector.add(unit.getPipe_page());
        vector.add(inserttime);
        return vector;
    }

    private int bytes2int(int off, int length, byte[] bytes) {
        int i = 0;
        for (int j = 0; j < length; j++) {
            i |= (bytes[off + j] & 0xff) << (j * 8);
        }
        return i;
    }

    @Override
    public int compareTo(DisDataBean o) {
        return this.sj - o.sj > 0 ? 1 : -1;
    }
}
