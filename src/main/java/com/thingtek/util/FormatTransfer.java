package com.thingtek.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

public class FormatTransfer {

    /*private static int cal_serv_crc(byte[] message, int off, int len) {
        int crc = 0x00;
        int polynomial = 0x1021;
        for (int index = off; index < off + len; index++) {
            byte b = message[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
    }

    public static void calcCRC16_X(byte[] b) {
        int crc16 = cal_serv_crc(b, 0, b.length - 2);
        b[b.length - 1] = (byte) (crc16 & 0xFF);
        b[b.length - 2] = (byte) (crc16 >> 8 & 0xFF);
    }

    public static boolean checkCRC16_X(byte[] bytes) {
        int crc16 = cal_serv_crc(bytes, 1, bytes.length - 4);
        System.out.print("\n校验：" + Integer.toHexString(crc16));
        return (bytes[bytes.length - 2] == (byte) (crc16 & 0xFF))
                && (bytes[bytes.length - 3] == (byte) (crc16 >> 8 & 0xFF));
    }*/

    private static int bytes2Int(byte b[]) {
        return bytes2Int(b, 0, b.length);
    }

    private static int bytes2Int(byte b[], int off, int len) {
        int temp = 0;
        for (int i = off; i < off + len; i++) {
            temp = temp << 8 | (b[i] & 0xFF);
        }
        return temp;
    }

    public static int getDataLen(byte... b) {
        return bytes2Int(b);
    }

    public static Float bytesL2Float3(byte[] b, int off, int len, int newScale) {
        short temp = 0;
        for (int i = off; i < off + len; i++) {
            temp = (short) (temp | (b[i] & 0xFF) << (8 * (i - off)));
        }
        float temp_float = (float) (temp * 1.0 / newScale);
        int temp_int = Math.round(temp_float * 100);
        return (float) (temp_int / 100.0);
    }

    synchronized static Float bytesL2Float2(byte[] b) {
        int temp = 0;
        for (int i = 0; i < 4; i++) {
            temp = temp | (b[i] & 0xFF) << (8 * i);
        }
        Float f = Float.intBitsToFloat(temp);
        System.out.print(Arrays.toString(b));
        System.out.print(f);
        if (Float.isNaN(f)) {
            return 0f;
        }
        BigDecimal bd = new BigDecimal(f);
        return bd.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static byte[] float2Bytes(float f) {
        byte[] bytes = new byte[4];
        int i = Float.floatToIntBits(f);
        // System.out.println(i);
        for (int j = 0; j < bytes.length; j++) {
            bytes[j] = (byte) ((i >> (j * 8)) & 0xff);
        }
        return bytes;
    }

    public static float newScale(float f1, float f2, int scale) {
        float f3 = f1 - f2;
        int scaleresult = (int) Math.pow(10, scale);
        int i1 = Math.round(f3 * scaleresult);
        return (float) (i1 / 10.0);
    }

    public static String float2String2(float f) {
        DecimalFormat dFormat = new DecimalFormat("#0.00");
        return dFormat.format(f);
    }

    public static String float2String(float f) {
        DecimalFormat dFormat = new DecimalFormat("#0.0");
        return dFormat.format(f);
    }

}
