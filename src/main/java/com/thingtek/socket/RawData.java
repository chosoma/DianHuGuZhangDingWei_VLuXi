package com.thingtek.socket;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import lombok.Data;

import java.util.Date;

@Data
public class RawData {

    private int bigseq;//大包序列
    private int smallseq;//小包序列
    private int totalsmallseq;//小包总包数
    private DisDataBean data;
    private Date time;

    public RawData(int bigseq, int smallseq, int totalsmallseq, DisDataBean data) {
        this.bigseq = bigseq;
        this.smallseq = smallseq;
        this.totalsmallseq = totalsmallseq;
        this.data = data;
    }
}
