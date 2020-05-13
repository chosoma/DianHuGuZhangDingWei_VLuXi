package com.thingtek.view.shell;

import com.thingtek.beanServiceDao.pipe.service.PipeService;
import com.thingtek.beanServiceDao.unit.service.LXUnitService;
import com.thingtek.beanServiceDao.warn.service.WarnService;
import com.thingtek.view.component.factory.Factorys;
import com.thingtek.view.logo.LogoInfo;

import javax.annotation.Resource;
import javax.swing.*;

public abstract class BasePanel extends JPanel {
    @Resource
    protected LogoInfo logoInfo;
    private boolean show;
    private boolean warn;


    public boolean isAdmin() {
        return logoInfo.isAdmin();
    }

    public boolean isWarn() {
        return warn;
    }

    public void setWarn(boolean warn) {
        this.warn = warn;
    }

    public boolean isShow() {
        return show;
    }

    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    @Resource
    protected Factorys factorys;
    @Resource
    protected LXUnitService unitService;
    @Resource
    protected PipeService pipeService;
    @Resource
    protected WarnService warnService;

    public abstract BasePanel init();

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
