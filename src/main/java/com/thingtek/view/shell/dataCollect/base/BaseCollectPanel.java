package com.thingtek.view.shell.dataCollect.base;

import com.thingtek.beanServiceDao.data.base.BaseDataBean;
import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.beanServiceDao.warn.service.WarnService;
import com.thingtek.view.shell.BasePanel;

import javax.annotation.Resource;

public abstract class BaseCollectPanel extends BasePanel {

    protected boolean defaultselect;

    protected boolean admin;

    public void setDefaultselect(boolean defaultselect) {
        this.defaultselect = defaultselect;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isDefaultselect() {
        return defaultselect;
    }

    protected int clttype;

    public int getClttype() {
        return clttype;
    }

    @Override
    public BaseCollectPanel init() {
        setBackground(factorys.getColorFactory().getColor("collectback"));
        return this;
    }

    public abstract void refreshPoint();

    /*
    添加数据之前需要设置单元
     */

    public abstract void addWarn(DisDataBean warnBean);

    public abstract void refreshData();

}
