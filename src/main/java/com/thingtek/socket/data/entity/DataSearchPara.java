package com.thingtek.socket.data.entity;

import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import lombok.Data;

import java.util.Date;
import java.util.List;

public @Data
class DataSearchPara {

    private Byte unit_num;

    private BaseUnitBean unit;

    private List<Byte> unitnums;

    private int startcount;

    private Date T1;

    private Date T2;

    private int clttype;

    private int thispage;//当前页
    private int endpage;//尾页
    private int searchstart;
    private int onepagecount;

    public void tofirst() {
        thispage = 0;
        setsearchstart();
    }

    public void tonext() {
        thispage++;
        setsearchstart();
    }

    public void toback() {
        thispage--;
        if (thispage < 0) {
            thispage = 0;
        }
        setsearchstart();
    }

    private void setsearchstart() {
        searchstart = thispage * onepagecount;
    }

}
