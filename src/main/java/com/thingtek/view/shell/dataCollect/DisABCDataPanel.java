package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;

import javax.swing.*;
import java.awt.*;

public class DisABCDataPanel extends JPanel {
    private String[] phases;
    private Color[] colors;
    private Color colorWarn = new Color(255, 80, 0),
            colorB = new Color(255, 255, 255),
            buttonColor = new Color(240, 240, 240);
    private JLabel[] jldatas;

    public DisABCDataPanel() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());

        phases = new String[]{"A", "B", "C"};
        colors = new Color[]{
                new Color(158, 158, 0),
                new Color(0, 158, 0),
                new Color(158, 0, 0)};
        setVisible(false);
        initTitle();
        initLeft();
        initCenter();
    }

    private void initCenter() {
        JPanel center = new JPanel(new GridLayout(phases.length, 1));
        jldatas = new JLabel[phases.length];
        for (int i = 0; i < phases.length; i++) {
            jldatas[i] = new JLabel("  ", JLabel.CENTER);
            jldatas[i].setForeground(colors[i]);
            center.add(jldatas[i]);
        }
        this.add(center, BorderLayout.CENTER);
    }

    private void initLeft() {
        JPanel left = new JPanel(new GridLayout(phases.length, 1));

        Font font = new Font(null, Font.PLAIN, 25);
        JLabel[] jlphase = new JLabel[phases.length];
        for (int i = 0; i < jlphase.length; i++) {
            jlphase[i] = new JLabel(phases[i], JLabel.CENTER);
            jlphase[i].setFont(font);
            jlphase[i].setForeground(colors[i]);
            left.add(jlphase[i]);
        }
        this.add(left, BorderLayout.WEST);
    }

    private JLabel title;

    private void initTitle() {
        title = new JLabel(" ", JLabel.CENTER);
        this.add(title, BorderLayout.NORTH);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void startWarn(DisUnitBean warnBean) {
        int index = getIndex(warnBean.getPhase());
        if (index < 0) return;
        JLabel jLabel = jldatas[index];
        jLabel.setText("异常");
        jLabel.setBackground(colorWarn);
        jLabel.setOpaque(true);
    }

    public void removeWarn() {
        for (int i = 0; i < jldatas.length; i++) {
            JLabel jLabel = jldatas[i];
            jLabel.setText("");
            jLabel.setBackground(colorB);
            jLabel.setOpaque(false);
        }
    }

    private int getIndex(String phase) {
        int index = -1;
        for (int i = 0; i < phases.length; i++) {
            if (phases[i].equals(phase)) {
                return i;
            }
        }
        return index;
    }

}
