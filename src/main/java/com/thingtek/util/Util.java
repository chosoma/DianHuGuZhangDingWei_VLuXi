package com.thingtek.util;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;

import com.thingtek.view.component.factory.MyColorFactory;
import sun.swing.SwingUtilities2;

public class Util {

    public static int TableRowHeight = 22;// 表行高
    public static int TableHeadHeight = 24;// 表头高

    public static String DATA_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String DATA_FORMAT_PATTERN_2 = "yyyy-MM-dd HH:mm";
    public static String DATA_FORMAT_PATTERN_3 = "yyyy-MM-dd";

    public static AlphaComposite AlphaComposite_100 = AlphaComposite.SrcOver;
    public static AlphaComposite AlphaComposite_50F = AlphaComposite.SrcOver.derive(0.5f);
    public static AlphaComposite AlphaComposite_30F = AlphaComposite.SrcOver.derive(0.3f);
    public static AlphaComposite AlphaComposite_20F = AlphaComposite.SrcOver.derive(0.2f);
    public static AlphaComposite AlphaComposite_8F = AlphaComposite.SrcOver.derive(0.08f);

    public static Date startDate = Timestamp.valueOf("1970-01-01 00:00:00");
    public static Date endDate = Timestamp.valueOf("2099-01-01 00:00:00");

    public static void DrawShadow(Graphics2D g2, Color c, int x, int y, int w,
                                  int h) {
        Composite composite = g2.getComposite();
        if (c == null) {
            g2.setColor(new Color(120, 120, 120));
        } else
            g2.setColor(c);
        // line
        g2.setComposite(AlphaComposite_50F);// dark
        g2.drawLine(2 + x, h - 2, w - 3, h - 2);// HORIZONTAL
        g2.drawLine(w - 2, 2 + y, w - 2, h - 3);// VERTICAL
        g2.setComposite(AlphaComposite_30F);// light
        g2.drawLine(2 + x, h - 1, w - 3, h - 1);// HORIZONTAL
        g2.drawLine(w - 1, 2 + y, w - 1, h - 3);// VERTICAL
        // conner-dark
        g2.drawLine(w - 2, 1 + y, w - 2, 1 + y);// rightTop
        g2.drawLine(w - 2, h - 2, w - 2, h - 2);// rightBottom
        g2.drawLine(1 + x, h - 2, 1 + x, h - 2);// leftBottom
        // conner-light
        g2.setComposite(AlphaComposite_20F);
        g2.drawLine(w - 2, y, w - 1, 1 + y);// rightTop
        g2.drawLine(w - 1, h - 2, w - 2, h - 1);// rightBottom
        g2.drawLine(1 + x, h - 1, x, h - 2);// leftBottom
        // conner
        // g2.setComposite(AC.derive(0.08f));
        // g2.drawLine(w - 1, y, w - 1, y);// rightTop
        // g2.drawLine(w - 1, h - 1, w - 1, h - 1);// rightBottom
        // g2.drawLine(x, h - 1, x, h - 1);// leftBottom
        g2.setComposite(composite);
    }
    // 绘制阴影
    public static void DrawBorder(Graphics2D g2, Color c, int width, int height) {
        g2.setColor(c);
        g2.drawRect(0, 0, width - 3, height - 3);
        DrawShadow(g2, null, 0, 0, width, height);
    }
}