package com.thingtek.view.shell.dataCollect.base;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.view.shell.BasePanel;

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
