package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.point.entity.PointBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;

public class ChartIconLabel extends JLabel {

    private PointBean pointBean;

    private JPanel dataPanel;
    private Color colorWarn = new Color(255, 80, 0),
            colorB = new Color(255, 255, 255);

    public ChartIconLabel(String name, ImageIcon icon) {
//        super(name, icon);
        super(icon);
        this.setText(name);
        this.setForeground(Color.WHITE);
        this.setBackground(colorWarn);
        setOpaque(false);
    }

    public void setPointBean(PointBean pointBean) {
        this.pointBean = pointBean;
    }

    public PointBean getPointBean() {
        return pointBean;
    }

    public void addListeners(EventListener l) {
        this.addMouseListener((MouseListener) l);
        this.addMouseMotionListener((MouseMotionListener) l);
    }

//    private Thread warnThread;


    public void startWarning() {
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

    public void stopWarning() {
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

    public void setDataPanel(JPanel dataPanel) {
        this.dataPanel = dataPanel;
    }

    public JPanel getDataPanel() {
        return dataPanel;
    }

}
