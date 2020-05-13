package com.thingtek.beanServiceDao.base;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import org.apache.log4j.Logger;

import java.io.IOException;


public abstract class BaseService {

    protected void log(Exception e) {
        e.printStackTrace();
        Logger.getLogger(e.getClass()).error(e.toString());
    }
}
