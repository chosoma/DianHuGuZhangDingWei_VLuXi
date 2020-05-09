package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;

public class LXUnitIconLabel extends JLabel {

    private LXUnitBean unitBean;

    LXUnitIconLabel(String name, ImageIcon icon) {
//        super(name, icon);
        super(icon);
        this.setText(name);
        this.setForeground(Color.WHITE);
        Color colorWarn = new Color(255, 80, 0);
        this.setBackground(colorWarn);
        setOpaque(false);
    }

    LXUnitBean getUnitBean() {
        return unitBean;
    }

    void setUnitBean(LXUnitBean unitBean) {
        this.unitBean = unitBean;
    }

    void addListeners(EventListener l) {
        this.addMouseListener((MouseListener) l);
        this.addMouseMotionListener((MouseMotionListener) l);
    }

//    private Thread warnThread;


    void startWarning() {
        setOpaque(true);
        validate();
        invalidate();
        updateUI();
        /*warnThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    setBackground(colorWarn);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setBackground(colorB);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        warnThread.start();*/
    }

    void stopWarning() {
//        warnThread.interrupt();
        setOpaque(false);
        validate();
        invalidate();
        updateUI();
    }

    /*@Override
    public Border getBorder() {
        return null;
    }*/

    @Override
    public int getVerticalTextPosition() {
        return JButton.BOTTOM;
    }

    @Override
    public int getHorizontalTextPosition() {
        return JButton.CENTER;
    }

}
