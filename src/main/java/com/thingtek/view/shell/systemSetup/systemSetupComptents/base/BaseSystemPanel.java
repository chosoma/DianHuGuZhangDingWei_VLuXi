package com.thingtek.view.shell.systemSetup.systemSetupComptents.base;

import com.thingtek.beanServiceDao.clt.service.CltService;
import com.thingtek.beanServiceDao.pipe.service.PipeService;
import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.view.component.button.EditButton;
import com.thingtek.view.component.factory.Factorys;
import com.thingtek.view.logo.LogoInfo;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;

public abstract class BaseSystemPanel extends JPanel {

    protected boolean show;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    @Resource
    protected Factorys factorys;

    @Resource
    protected UnitService unitService;

    @Resource
    protected PointService pointService;

    @Resource
    protected CltService cltService;

    @Resource
    protected LogoInfo logoInfo;

    @Resource
    protected PipeService pipeService;

    /**
     * 设置标题
     *
     * @param text 文本
     */
    public void setTitle(String text) {
        titleLabel.setText(text);
    }

    private JPanel centerPanel, toolbar;
    /*protected int x, y, height = 30, yheight = 60, widthlabel = 100, widthinput = 300;*/

    private JLabel titleLabel;

    public BaseSystemPanel() {
        setBackground(Color.WHITE);
    }

    public BaseSystemPanel init() {
        this.setLayout(new BorderLayout());
        initTitle();
        initCenter();
        initToolbar();
        return this;
    }

    /**
     * 初始化标题
     */
    protected void initTitle() {
        titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(factorys.getFontFactory().getFont("systemSetupTitle"));
        titleLabel.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));// 绘制空白边框
        this.add(titleLabel, BorderLayout.NORTH);
    }

    /**
     * 初始化中心
     */
    protected void initCenter() {
//        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        this.centerPanel.setBorder(factorys.getBorderFactory().getLineBorder("component_Border"));
//        this.centerPanel.setPreferredSize(new Dimension(500, 320));
//        center.add(this.centerPanel);
//        this.add(center, BorderLayout.CENTER);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * 初始化按钮栏
     */
    protected void initToolbar() {
        toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        toolbar.setOpaque(false);
        toolbar.setBorder(factorys.getBorderFactory().getLineBorder("component_Border"));
        this.add(toolbar, BorderLayout.SOUTH);
    }

    /**
     * 工具栏添加按钮
     *
     * @param text 文本
     * @param icon 图标
     * @return 按钮对象
     */
    protected EditButton addTool(String text, String icon) {
        EditButton editButton = new EditButton(text, factorys.getIconFactory().getIcon(icon));
        editButton.setToolTipText(text);
        toolbar.add(editButton);
        return editButton;
    }

    protected void addTool(Component component) {
        toolbar.add(component);
    }


    /**
     * 自由布局
     *
     * @param component 组件
     * @param x         X坐标
     * @param y         Y坐标
     * @param width     宽度
     * @param height    高度
     */
    protected void addCenter(Component component, int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
        component.setFont(factorys.getFontFactory().getFont("systemSetupComponentFont"));
        if (component instanceof JComboBox) {
            component.setEnabled(false);
        }
        centerPanel.add(component);
    }

    /**
     * 箱式布局
     *
     * @param component 组件
     * @param place     位置
     */
    protected void addCenter(Component component, String place) {
        centerPanel.add(component, place);
    }

    /**
     * 箱式布局中心
     *
     * @param component 组件
     */
    protected void addCenter(Component component) {
        centerPanel.add(component, BorderLayout.CENTER);
    }

    /**
     * 错误弹窗
     *
     * @param text 文本信息
     */
    protected void errorMessage(String text) {
        JOptionPane.showMessageDialog(null, text, "错误", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 失败弹窗
     *
     * @param text 文本信息
     */
    protected void falseMessage(String text) {
        JOptionPane.showMessageDialog(null, text, "失败", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 成功弹窗
     *
     * @param text 文本信息
     */
    protected void successMessage(String text) {
        JOptionPane.showMessageDialog(null, text, "成功", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 检查是否为数字
     *
     * @param text 数字字符串
     * @return true 是数字 false 不是数字
     */
    protected boolean isNum(String text) {
        return text.matches("^([1-9]\\d*|0)(\\.\\d{1,9})?$");
    }

    protected boolean isInt(String text) {
        return text.matches("^([1-9]\\d*|0)$");
    }

    protected boolean isIp(String text) {
        String ipreg = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";
        return text.matches(ipreg);
    }

    public abstract void loading();
    /*public void refresh() {}*/

}
