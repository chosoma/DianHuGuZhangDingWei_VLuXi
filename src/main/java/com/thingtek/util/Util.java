package com.thingtek.util;

import java.awt.*;
import java.sql.Timestamp;
import java.util.Date;

public class Util {

    public static int TableRowHeight = 22;// 表行高
    public static int TableHeadHeight = 24;// 表头高

    public static String DATA_FORMAT_PATTERN_3 = "yyyy-MM-dd";

    public static AlphaComposite AlphaComposite_100 = AlphaComposite.SrcOver;
    public static AlphaComposite AlphaComposite_50F = AlphaComposite.SrcOver.derive(0.5f);

    public static Date startDate = Timestamp.valueOf("1970-01-01 00:00:00");
    public static Date endDate = Timestamp.valueOf("2099-01-01 00:00:00");

}