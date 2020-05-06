package com.thingtek.socket.entity;

import com.thingtek.socket.DataBuffer;

public abstract class BaseG2S extends BaseP2P {

    protected boolean cansend;

    public boolean isCansend() {
        return cansend;
    }

    protected DataBuffer dataBuffer;

    public void setDataBuffer(DataBuffer dataBuffer) {
        this.dataBuffer = dataBuffer;
    }

    protected byte[] bytes;

    public BaseG2S setBytes(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }

    @Override
    public void resolve() {
        unitnum = (short) bytes2int(unitnumoff, unitnumlength, bytes);
    }

    public byte[] getResult() {
        return new byte[0];
    }

}