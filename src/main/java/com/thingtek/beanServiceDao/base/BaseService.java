package com.thingtek.beanServiceDao.base;

import org.apache.log4j.Logger;

public abstract class BaseService {

    protected void log(Exception e) {
        e.printStackTrace();
        Logger.getLogger(e.getClass()).error(e.toString());
    }
}
