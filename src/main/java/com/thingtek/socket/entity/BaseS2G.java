package com.thingtek.socket.entity;

public abstract class BaseS2G extends BaseP2P {

    protected byte cmdtype;

    public void setCmdtype(byte cmdtype) {
        this.cmdtype = cmdtype;
    }

    protected byte[] datas;

    public void setDatas(byte[] datas) {
        this.datas = datas;
    }

    @Override
    public void resolve() {

    }


    public byte[] getResult() {
        if (datas == null) {
            datas = new byte[]{};
        }
        byte[] result = new byte[datas.length + 5];
        result[0] = cmdtype;
        result[1] = (byte) (unitnum & 0xff);
        result[2] = (byte) (unitnum >> 8 & 0xff);
        System.arraycopy(datas, 0, result, 3, datas.length);
        return calcCRC16_X(result);
    }

}
