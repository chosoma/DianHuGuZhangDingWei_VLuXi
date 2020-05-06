package com.thingtek.view.component.dialog.base;

import com.thingtek.view.component.factory.Factorys;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDialog extends JDialog {

    protected JPanel container;
    protected Factorys factorys;


    public void setFactorys(Factorys factorys) {
        this.factorys = factorys;
    }

    public BaseDialog(JFrame jFrame, String titleText, Image icon) {
        super(jFrame, titleText, true);
        setTitle(titleText);
        setIconImage(icon);
        setResizable(false);
    }

    public BaseDialog initDialog() {
        container = new JPanel(new BorderLayout());
//        container.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setContentPane(container);
        initCenter();
        initTool();
        return this;
    }


    public abstract void initCenter();

    protected JButton buttonSave;

    public abstract void initTool();

    public void visible() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


}
