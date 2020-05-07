package com.thingtek.view.component.tablecellrander;

import com.thingtek.util.Util;
import com.thingtek.view.component.factory.Factorys;
import com.thingtek.view.component.tablemodel.BaseTableModel;
import com.thingtek.view.component.tablemodel.TableConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
public @Data
class TCR extends DefaultTableCellRenderer {

    private String collect_name;

    @Resource
    private Factorys factorys;
    @Resource
    private TableConfig tableConfig;

    public TCR() {
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    private Color colortrue = new Color(50, 200, 50), colorfalse = new Color(200, 50, 50);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.isSelected = isSelected;
        if (isSelected) {
            setForeground(UIManager.getColor("Table.selectionForeground"));
        } else {
            if (row % 2 == 0) {
                setForeground(factorys.getColorFactory().getColor("tableOdd_F"));
                setBackground(factorys.getColorFactory().getColor("tableOdd_B"));
            } else {
                setForeground(factorys.getColorFactory().getColor("tableEven_F"));
                setBackground(factorys.getColorFactory().getColor("tableEven_B"));
            }
        }
        setFont(table.getFont());

        String s = null;
        if (value != null) {
            if (value instanceof Date) {
                Date t = (Date) value;
                s = new SimpleDateFormat(tableConfig.getDatereg()).format(t);
            } else if (value instanceof Integer) {
                s = new DecimalFormat("#0").format(value);
            } else if (value instanceof Float || value instanceof Double) {
                s = new DecimalFormat("#0.00").format(value);
//                s = new DecimalFormat(tableConfig.getDecimalReg(collect_name)).format(value);
            } else if (value instanceof Boolean) {
                boolean bool = (boolean) value;
                if (bool) {
                    setBackground(colortrue);
                } else {
                    setBackground(colorfalse);
                }
            } else {
                s = value.toString();
            }
        }
        setText(s);
        return this;
    }

    private boolean isSelected = false;

    @Override
    protected void paintComponent(Graphics g) {
//        if (isSelected) {
//            setOpaque(false);
//            Graphics2D g2 = (Graphics2D) g.create();
//            g2.setPaint(
//                    new GradientPaint(0, 1,
//                            factorys.getColorFactory().getColor("tableSelectTop"),
//                            0, getHeight() - 1,
//                            factorys.getColorFactory().getColor("tableSelectBottom")));
//            g2.fillRect(0, 0, getWidth(), getHeight());
//            g2.dispose();
//        } else {
//            setOpaque(true);
//        }

        super.paintComponent(g);
    }

    public void initializeTable(JTable table) {
        table.setDefaultRenderer(String.class, this);
        table.setDefaultRenderer(Number.class, this);
        table.setDefaultRenderer(Float.class, this);
        table.setDefaultRenderer(Integer.class, this);
        table.setDefaultRenderer(Double.class, this);
        table.setDefaultRenderer(Date.class, this);
        table.setDefaultRenderer(Object.class, this);
        // 表头设置
        JTableHeader tableHeader = table.getTableHeader();
        DefaultTableCellRenderer dtcr = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);// 表头居中

        Dimension dimension = dtcr.getSize();
        dimension.height = Util.TableHeadHeight;
        dtcr.setPreferredSize(dimension);// 设置表头高度
        tableHeader.setDefaultRenderer(dtcr);
        // 表头不可拖动
        tableHeader.setReorderingAllowed(false);
        // 列宽可修改
        tableHeader.setResizingAllowed(false);
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // 表自动排序
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(Util.TableRowHeight);// 设置行高
    }

    public void initializeTable(JTable table, BaseTableModel tableModel) {
        table.setDefaultRenderer(String.class, this);
        table.setDefaultRenderer(Number.class, this);
        table.setDefaultRenderer(Float.class, this);
        table.setDefaultRenderer(Integer.class, this);
        table.setDefaultRenderer(Double.class, this);
        table.setDefaultRenderer(Date.class, this);
        table.setDefaultRenderer(Object.class, this);
        // 表头设置
        JTableHeader tableHeader = table.getTableHeader();
        DefaultTableCellRenderer dtcr = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);// 表头居中

        Dimension dimension = dtcr.getSize();
        dimension.height = Util.TableHeadHeight;
        dtcr.setPreferredSize(dimension);// 设置表头高度
        tableHeader.setDefaultRenderer(dtcr);
        // 表头不可拖动
        tableHeader.setReorderingAllowed(false);
        // 列宽可修改
        tableHeader.setResizingAllowed(false);
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // 表自动排序
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(Util.TableRowHeight);// 设置行高
        TableColumnModel columnModel = table.getColumnModel();
        int[] widthes = tableModel.getColumnWidthes();
        for (int i = 0; i < widthes.length; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(widthes[i]);
        }
    }
}
