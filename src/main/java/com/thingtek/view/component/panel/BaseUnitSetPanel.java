package com.thingtek.view.component.panel;

import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public @Data
abstract class BaseUnitSetPanel extends JPanel {
    protected String[] titles;
    protected String[] prompt;
    protected Font font;

    protected Map<String, Integer> componentIndexs;
    protected JLabel[] labels;

    protected DisUnitBean unitBean;
    protected GridBagConstraints gbc;
    protected GridBagLayout gbl;
    protected JPanel center;

    public void init() {
        setLayout(new BorderLayout());
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;

        JPanel jPanel = new JPanel(gbl);
        this.add(jPanel, BorderLayout.WEST);
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel[] promptLabels = new JLabel[prompt.length];
        for (int i = 0; i < promptLabels.length; i++, gbc.gridy++) {
            promptLabels[i] = new JLabel(prompt[i], JLabel.CENTER);
            promptLabels[i].setFont(font);
            gbl.setConstraints(promptLabels[i], gbc);
            jPanel.add(promptLabels[i]);
        }

        center = new JPanel(gbl);
        this.add(center, BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        JTextArea[] titleFields = new JTextArea[titles.length];
        for (int i = 0; i < titleFields.length; i++, gbc.gridx++) {
            titleFields[i] = new JTextArea(titles[i]);
            titleFields[i].setEditable(false);
            titleFields[i].setOpaque(false);
            titleFields[i].setFont(font);
            gbl.setConstraints(titleFields[i], gbc);
            center.add(titleFields[i]);
        }

        gbc.gridx = 0;
        gbc.gridy++;
        labels = new JLabel[titles.length];
        for (int i = 0; i < labels.length; i++, gbc.gridx++) {
            labels[i] = new JLabel();
            labels[i].setHorizontalTextPosition(JLabel.CENTER);
            labels[i].setFont(font);
            gbl.setConstraints(labels[i], gbc);
            center.add(labels[i]);
        }
        gbc.gridx = 0;
        gbc.gridy++;
    }

    protected void initValue() {
        Set<Map.Entry<String, Integer>> entries = componentIndexs.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            Object result = unitBean.get(entry.getKey());
            labels[entry.getValue()].setText(String.valueOf(result == null ? 0 : result));
        }
    }

    public abstract DisUnitBean getValues();

}
