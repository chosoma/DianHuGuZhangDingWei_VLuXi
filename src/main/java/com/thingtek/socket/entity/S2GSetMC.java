package com.thingtek.socket.entity;

public class S2GSetMC extends BaseS2G {
    public S2GSetMC() {
        cmdtype = (byte) 0xff;
    }

    public byte[] getResult() {
        if (datas == null) {
            datas = new byte[]{};
        }
        byte[] result = new byte[datas.length +3];
        result[0] = cmdtype;
        System.arraycopy(datas, 0, result, 1, datas.length);
        return calcCRC16_X(result);
    }
}
