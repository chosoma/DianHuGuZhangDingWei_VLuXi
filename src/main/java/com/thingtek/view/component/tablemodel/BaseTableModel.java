package com.thingtek.view.component.tablemodel;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector;

public abstract class BaseTableModel extends DefaultTableModel {

    Vector<Vector<Object>> row;

    protected Class[] typeArray;
    protected int[] columnWidthes;

    public int[] getColumnWidthes() {
        return columnWidthes;
    }

    public BaseTableModel() {
        initDefault();
    }

    protected void initDefault() {
        row = new Vector<>();
    }

    /**
     * 添加数据
     */
    public void addDatas(List<Vector<Object>> datas) {
        row.clear();
        row.addAll(datas);
        this.fireTableDataChanged();
    }

    public void addData(Vector<Object> datas) {
        row.add(datas);
        this.fireTableDataChanged();
    }

    /**
     * 删除数据
     */
    public void removeRows(int[] selRows) {
        // 反向循环实现删除
        for (int i = selRows.length - 1; i >= 0; i--) {
            // 上句会出现删除最后一行出错
            removeRow(selRows[i]);
        }
    }

    /**
     * 清空数据
     */
    public void clearData() {
        row.clear();
        this.fireTableDataChanged();
    }

    /**
     * 设置JTable不可修改
     */
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * 重写getColumnClass方法，实现排序对列类型的区分
     * 这里根据数据库表中各个列类型，自定义返回每列的类型(用于解决数据库中NULL处理抛出异常)
     */
    @Override
    public Class<?> getColumnClass(int column) {
        return typeArray[column];
    }

}
