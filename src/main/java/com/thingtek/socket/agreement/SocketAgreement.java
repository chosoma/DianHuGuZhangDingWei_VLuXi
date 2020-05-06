package com.thingtek.socket.agreement;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.socket.entity.BaseG2S;
import com.thingtek.socket.entity.BaseS2G;
import com.thingtek.socket.DataBuffer;
import com.thingtek.socket.Protocol;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public @Data
class SocketAgreement extends BaseService {

    //    private byte[] net;
    private byte[] head;
    private int headoff;
    private byte[] cmdtype;
    private int cmdtypeoff;

    private int totallength;
    private byte[] tail;
    private int tailoff;

    public static final int HasHeadNoTail = -1;
    public static final int NoHeadHasTail = -2;
    public static final int NoHeadNoTail = -3;
    public static final int HasHeadHasTail = -4;

    private Map<Integer, String> G2S;

    private Map<String, String> s2gmap;

    private DataBuffer dataBuffer;

    public int getstartoff(byte[] bytes) {
        int startoff = -1;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == Protocol.HEAD) {
                startoff = i;
                break;
            }
        }
        return startoff;
    }

    public int getendoff(byte[] bytes) {
        int endoff = -1;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == Protocol.TAIL) {
                endoff = i;
                break;
            }
        }
        return endoff;
    }

    public int contains(byte[] bytes) {
        boolean headflag = false;
        boolean tailflag = false;
        for (byte aByte : bytes) {
            if (aByte == Protocol.HEAD) {
                headflag = true;
                continue;
            }
            if (aByte == Protocol.TAIL) {
                tailflag = true;
            }
        }
        if (headflag && tailflag) {
            return HasHeadHasTail;
        }
        if (headflag) {
            return HasHeadNoTail;
        }
        if (tailflag) {
            return NoHeadHasTail;
        }
        return NoHeadNoTail;
    }

    /*
        获取数据头下标
     */
    public int getHeadIndex(byte[] bytes) {
        int off = -1;
        for (int i = 0, j = 0; i < bytes.length; i++) {
            if (bytes[i] == head[j]) {
                off = i;
                j++;
            } else {
                j = 0;
            }
            if (j >= head.length) {
                break;
            }
        }
        return off - head.length + 1;
    }

    public synchronized BaseG2S getG2S(byte[] bytes) {
        int cmds = 0;
        for (int i = 0; i < this.cmdtype.length; i++) {
            cmds |= (bytes[cmdtype[i]] & 0xff) << (i * 8);
        }
        try {
            BaseG2S g2s = (BaseG2S) this.getClass()
                    .getClassLoader()
                    .loadClass(G2S.get(cmds))
                    .newInstance();
            g2s.setAgreementConfig(this);
            g2s.setDataBuffer(dataBuffer);
            byte[] b = new byte[bytes.length - cmdtype.length];
            System.arraycopy(bytes, cmdtypeoff + 1, b, 0, b.length);
            return g2s.setBytes(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized BaseS2G getS2G(String keycmd) {
        BaseS2G s2g = null;
        try {
            s2g = (BaseS2G) this.getClass()
                    .getClassLoader()
                    .loadClass(s2gmap.get(keycmd))
                    .newInstance();
            s2g.setAgreementConfig(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s2g;
    }

    private int cal_serv_crc(byte[] message, int len) {
        int crc = 0x00;
        int polynomial = 0x1021;
        for (int index = 0; index < len; index++) {
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
        int crc16 = cal_serv_crc(b, b.length - 2);
        b[b.length - 1] = (byte) (crc16 & 0xFF);
        b[b.length - 2] = (byte) (crc16 >> 8 & 0xFF);
        return b;
    }

    public boolean checkCRC16_X(byte[] bytes) {
        int crc16 = cal_serv_crc(bytes, bytes.length - 2);
//        System.out.println("校验：" + Integer.toHexString(crc16));
        return (bytes[bytes.length - 1] == (byte) (crc16 & 0xFF))
                && (bytes[bytes.length - 2] == (byte) (crc16 >> 8 & 0xFF));
    }


    public int getCmd(byte[] bytes) {
        int cmd = 0;
        for (int i = 0; i < this.cmdtype.length; i++) {
            cmd |= (bytes[cmdtypeoff + cmdtype[i]] & 0xff) << (i * 8);
        }
        return cmd;
    }

}
