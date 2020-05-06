package com.thingtek.socket;

public class Protocol {

    /************ 转义 ***************/
    public final static byte HEAD = 0x7E;// 头
    public final static byte TAIL = 0x7D;// 尾
    public final static byte TURN = 0x7C;//
    public final static byte IST1 = 0x0D;//
    public final static byte IST2 = 0x0A;//

    public final static byte HEADT = 0x5E;// 头
    public final static byte TAILT = 0x5D;// 尾
    public final static byte TURNT = 0x5C;//
    public final static byte IST1T = 0x5B;//
    public final static byte IST2T = 0x5A;//
    /*********************************/

}
