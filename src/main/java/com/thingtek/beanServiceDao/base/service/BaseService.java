package com.thingtek.beanServiceDao.base.service;
import org.apache.log4j.Logger;

import javax.swing.*;

public abstract class BaseService {

    protected void log(Exception e) {
        e.printStackTrace();
        Logger.getLogger(e.getClass()).error(e.toString());
    }

    protected void logInfo(String object) {
        System.err.println(object);
        Logger.getLogger(object.getClass()).info(object);
    }

    protected void errorMessage(String text) {
        JOptionPane.showMessageDialog(null, text, "错误", JOptionPane.ERROR_MESSAGE);
    }

    protected void falseMessage(String text) {
        JOptionPane.showMessageDialog(null, text, "失败", JOptionPane.ERROR_MESSAGE);
    }

    protected void successMessage(String text) {
        JOptionPane.showMessageDialog(null, text, "成功", JOptionPane.INFORMATION_MESSAGE);
    }

}
