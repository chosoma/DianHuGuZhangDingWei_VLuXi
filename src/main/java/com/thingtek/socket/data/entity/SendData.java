package com.thingtek.socket.data.entity;


public class SendData {

    private int delay;
    private byte[] data;
    private int messageType;
    private int unit_num;


    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getUnit_num() {
        return unit_num;
    }

    public void setUnit_num(int unit_num) {
        this.unit_num = unit_num;
    }
}
