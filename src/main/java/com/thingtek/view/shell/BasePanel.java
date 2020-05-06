package com.thingtek.view.shell;

import com.thingtek.beanServiceDao.clt.service.CltService;
import com.thingtek.beanServiceDao.data.service.DisDataService;
import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.warn.service.WarnService;
import com.thingtek.socket.agreement.SocketAgreement;
import com.thingtek.view.component.factory.Factorys;
import com.thingtek.view.component.tablemodel.TableConfig;

import javax.annotation.Resource;
import javax.swing.*;

public abstract class BasePanel extends JPanel {

    private boolean show;
    private boolean warn;

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

    protected Integer authority;
    @Resource
    protected SocketAgreement agreementConfig;
    @Resource
    protected Factorys factorys;
    @Resource
    protected UnitService unitService;
    @Resource
    protected PointService pointService;
    @Resource
    protected CltService cltService;
    @Resource
    protected WarnService warnService;
    @Resource
    protected TableConfig tableConfig;

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
