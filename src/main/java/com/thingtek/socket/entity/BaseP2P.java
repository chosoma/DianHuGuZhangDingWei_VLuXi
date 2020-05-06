package com.thingtek.socket.entity;

import com.thingtek.beanServiceDao.data.service.DisDataService;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.socket.agreement.SocketAgreement;

import java.math.BigDecimal;

public abstract class BaseP2P {

    protected short unitnum;
    protected int unitnumoff = 0;
    protected int unitnumlength = 2;

    public void setUnitnum(short unitnum) {
        this.unitnum = unitnum;
    }

    public short getUnitnum() {
        return unitnum;
    }

    protected UnitService unitService;

    protected DisDataService dataService;

    public void setUnitService(UnitService unitService) {
        this.unitService = unitService;
    }

    public void setDataService(DisDataService dataService) {
        this.dataService = dataService;
    }

    protected SocketAgreement agreementConfig;

    public BaseP2P setAgreementConfig(SocketAgreement agreementConfig) {
        this.agreementConfig = agreementConfig;
        return this;
    }

    public abstract void resolve();

    public abstract byte[] getResult();

    private int cal_serv_crc(byte[] message, int off, int len) {
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

    protected byte[] calcCRC16_X(byte[] b) {
        int crc16 = cal_serv_crc(b, 0, b.length - 2);
        b[b.length - 1] = (byte) (crc16 & 0xFF);
        b[b.length - 2] = (byte) (crc16 >> 8 & 0xFF);
        return b;
    }

    protected byte[] int2bytes(int i, int length) {
        byte[] bytes = new byte[length];
        for (int j = 0; j < length; j++) {
            bytes[j] = (byte) (i >> (length - j - 1) * 8);
        }
        return bytes;
    }

    protected int bytes2int(int off, int length, byte[] bytes) {
        int i = 0;
        for (int j = 0; j < length; j++) {
            i |= (bytes[off + j] & 0xff) << (j * 8);
        }
        return i;
    }

    protected Float bytesL2Float2(byte[] b, int scale) {
        int temp = 0;
        for (int i = 0; i < 4; i++) {
            temp = temp | (b[i] & 0xFF) << (8 * i);
        }
        Float f = Float.intBitsToFloat(temp);
        if (Float.isNaN(f)) {
            return 0f;
        }
        BigDecimal bd = new BigDecimal(f);
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
