package com.thingtek.view.component.dialog.base;

import com.thingtek.beanServiceDao.pipe.service.PipeService;
import com.thingtek.beanServiceDao.unit.service.LXUnitService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class BaseSetDialog extends BaseDialog {

    protected LXUnitService unitService;

    protected PipeService pipeService;

    public BaseSetDialog(JFrame jFrame, String titleText, Image icon) {
        super(jFrame, titleText, icon);
    }

    public void setUnitService(LXUnitService unitService) {
        this.unitService = unitService;
    }

    public void setPipeService(PipeService pipeService) {
        this.pipeService = pipeService;
    }

    public BaseDialog initDialog() {
        return super.initDialog();
    }

    private JPanel centerPanel;

    public void initCenter() {
        centerPanel = new JPanel(null);
        container.add(centerPanel, BorderLayout.CENTER);
    }

    private int labelystart = -20;

    protected void addLabel(JComponent component) {
        component.setBounds(0, labelystart += 30, 80, 20);
        centerPanel.add(component);
    }

    private int intputystart = -20;

    protected void addInput(JComponent component) {
        component.setBounds(90, intputystart += 30, 130, 20);
        centerPanel.add(component);
    }

    @Override
    public void initTool() {
        JPanel bottomPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
        bottomPane.setBackground(new Color(240, 240, 240));
        container.add(bottomPane, BorderLayout.SOUTH);

        buttonSave = new JButton(factorys.getIconFactory().getIcon("apply"));
        buttonSave.setPreferredSize(new Dimension(100, 28));
        bottomPane.add(buttonSave);

        JButton buttonCancel = new JButton("取消", factorys.getIconFactory().getIcon("cancel"));
        buttonCancel.setToolTipText("取消");
        buttonCancel.setPreferredSize(new Dimension(100, 28));
        buttonCancel.addActionListener(e -> dispose());
        bottomPane.add(buttonCancel);
    }


    @Override
    public int getWidth() {
        return 300;
    }


    protected void setTotalSize(int ycount) {
        setSize(240, 76 + ycount * 30);
    }

}
