package com.thingtek.view.component.listener;


import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.beanServiceDao.unit.service.LXUnitService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class LXMouseMoveListenAdapter implements MouseListener, MouseMotionListener {
    private int x = 0;
    private int y = 0;

    private JComponent component;
    private JComponent parent;
    private LXUnitService unitService;
    private LXUnitBean unitBean;

    public void setUnitService(LXUnitService unitService) {
        this.unitService = unitService;
    }

    public void setUnitBean(LXUnitBean unitBean) {
        this.unitBean = unitBean;
    }

    public LXMouseMoveListenAdapter(JComponent component, JComponent parent) {
        this.component = component;
        this.parent = parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        System.out.println("clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
//        System.out.println("pressed");
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        System.out.println("released");
        x = 0;
        y = 0;
        Rectangle rectangle = component.getBounds();
        unitBean.setX(rectangle.getX() / parent.getWidth());
        unitBean.setY(rectangle.getY() / parent.getHeight());
        unitService.updateLXUnit(unitBean);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        System.out.println("entered");
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        System.out.println("exited");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        System.out.println("dragged");
        Point point = parent.getMousePosition();
        if (point != null) {
            component.setBounds((int) (point.getX() - x), (int) (point.getY() - y), component.getWidth(), component.getHeight());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        System.out.println("moved");
    }
}
