package com.thingtek.socket.entity;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.socket.RawData;

import java.util.Calendar;

public class G2SUploadData extends BaseG2S {

    @Override
    public void resolve() {
        super.resolve();
        DisDataBean data = new DisDataBean();
        data.setUnit_num(unitnum);
        int bigseqoff = 2, bigseqlength = 2, bigseq = bytes2int(bigseqoff, bigseqlength, bytes);
//        System.out.print("大:" + bigseq);
        int totalsmallseqoff = bigseqoff + 2, totalsmallseqlenght = 2, totalsmallseq = bytes2int(totalsmallseqoff, totalsmallseqlenght, bytes);
//        System.out.print(",小总:" + totalsmallseq);
        int smallseqoff = totalsmallseqoff + 2, smallseqlength = 2, smallseq = bytes2int(smallseqoff, smallseqlength, bytes);
//        System.out.println(",小:" + smallseq);
        int datalengthoff = smallseqoff + 2, datalengthlength = 2, datalength = bytes2int(datalengthoff, datalengthlength, bytes);
        byte[] datas;
        if (smallseq == totalsmallseq) {
            datas = new byte[datalength - 14];
            int xhqd = bytes2int(bytes.length - 14, 4, bytes);
            data.setXhqd(xhqd);
//            System.out.println("信号强度:" + xhqd);
            int sj = bytes2int(bytes.length - 10, 4, bytes);
            data.setGatewayfrontsj(sj);
//            System.out.println("时间:" + sj);
            int index = bytes2int(bytes.length - 6, 4, bytes);
            data.setGatewayfrontindex(index);
//            System.out.println("触发点:" + index);
        } else {
            datas = new byte[datalength];
        }
        int dataoff = datalengthoff + 2;
        System.arraycopy(bytes, dataoff, datas, 0, datas.length);
        data.resolve(datas);
        data.setInserttime(Calendar.getInstance().getTime());
        RawData rawdata = new RawData(bigseq, smallseq, totalsmallseq, data);
        BaseS2G s2g = agreementConfig.getS2G("uploaddata");
        if (dataBuffer.receDatas(rawdata)) {
            s2g.setCmdtype((byte) 0x10);
        } else {
            s2g.setCmdtype((byte) 0x11);
        }
        s2g.setUnitnum(unitnum);
        s2g.setDatas(new byte[]{});
        s2g.resolve();
        result = s2g.getResult();
        cansend = totalsmallseq == smallseq;
    }

    private byte[] result;

    @Override
    public byte[] getResult() {
        return result;
    }
}
