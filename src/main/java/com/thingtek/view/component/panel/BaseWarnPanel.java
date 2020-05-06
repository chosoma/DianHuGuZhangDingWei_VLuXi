package com.thingtek.view.component.panel;

import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public @Data
class BaseWarnPanel extends BaseUnitSetPanel {

    private JButton[][] operationButtons;

    private Map<String, Float> operationValues;
    private Map<String, Integer> scaleValues;
    private Map<String, java.util.List<Integer>> scopeValues;

    public void init() {
        super.init();
        operationButtons = new JButton[titles.length][];

        final Set<Map.Entry<String, Integer>> entries = componentIndexs.entrySet();
        for (final Map.Entry<String, Integer> entry : entries) {
            final JButton[] jButtons = new JButton[4];
            for (int i = 0; i < jButtons.length; i++) {
                jButtons[i] = new JButton();
            }
            operationButtons[entry.getValue()] = jButtons;
            JPanel jPanel = new JPanel(new GridLayout(2, 2));
            for (int i = 0; i < jButtons.length; i++) {
                jButtons[i].setFont(font);
                jPanel.add(jButtons[i]);
                final int index = i;
                jButtons[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Float nowvalue = Float.parseFloat(labels[entry.getValue()].getText());
                        Float one = operationValues.get(entry.getKey());
                        Integer scale = scaleValues.get(entry.getKey());
                        java.util.List<Integer> scope = scopeValues.get(entry.getKey());
                        Integer min = Collections.min(scope);
                        Integer max = Collections.max(scope);
                        switch (index) {
                            case 0:
                                nowvalue = scale(0, nowvalue, one, scale);
                                nowvalue = nowvalue <= min ? min : nowvalue;
                                break;
                            case 1:
                                nowvalue = scale(0, nowvalue, one * 10, scale);
                                nowvalue = nowvalue <= min ? min : nowvalue;
                                break;
                            case 2:
                                nowvalue = scale(1, nowvalue, one, scale);
                                nowvalue = nowvalue >= max ? max : nowvalue;
                                break;
                            case 3:
                                nowvalue = scale(1, nowvalue, one * 10, scale);
                                nowvalue = nowvalue >= max ? max : nowvalue;
                                break;
                        }
                        labels[entry.getValue()].setText(String.valueOf(nowvalue));
                    }
                });
                switch (i) {
                    case 0:
                        jButtons[i].setText("-");
                        break;
                    case 1:
                        jButtons[i].setText("--");
                        break;
                    case 2:
                        jButtons[i].setText("+");
                        break;
                    case 3:
                        jButtons[i].setText("++");
                        break;
                }
            }
            gbl.setConstraints(jPanel, gbc);
            center.add(jPanel);
            gbc.gridx++;
        }
    }

    @Override
    public void initValue() {
        super.initValue();
    }

    @Override
    public DisUnitBean getValues() {
        Map<String, Object> one = unitBean.getOne();
        Set<Map.Entry<String, Integer>> entries = componentIndexs.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            one.put(entry.getKey(), Float.valueOf(labels[entry.getValue()].getText()));
        }
        unitBean.resolve(one);
        return unitBean;
    }

    private float scale(int type, float f1, float f2, int scale) {
        float f3 = 0;
        switch (type) {
            case 0:
                f3 = f1 - f2;
                break;
            case 1:
                f3 = f1 + f2;
                break;
        }
        int s = (int) Math.pow(10, scale);
        int result = Math.round(f3 * s);
        return (float) (result * 1.0 / s);
    }

}
