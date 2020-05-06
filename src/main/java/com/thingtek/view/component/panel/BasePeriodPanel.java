package com.thingtek.view.component.panel;

import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public @Data
class BasePeriodPanel extends BaseUnitSetPanel {

    protected Map<String, String[]> comboxValueTypes;

    private JComboBox[] comboBoxes;

    public void init() {
        super.init();
        comboBoxes = new JComboBox[titles.length];
        Set<Map.Entry<String, Integer>> entries = componentIndexs.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            comboBoxes[entry.getValue()] = new JComboBox<String>(comboxValueTypes.get(entry.getKey()));
            comboBoxes[entry.getValue()].setFont(font);
            gbl.setConstraints(comboBoxes[entry.getValue()], gbc);
            center.add(comboBoxes[entry.getValue()]);
            gbc.gridx++;
        }
    }

    public void initValue() {
        super.initValue();
        Set<Map.Entry<String, Integer>> entries = componentIndexs.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            Object result = unitBean.get(entry.getKey());
            comboBoxes[entry.getValue()].setSelectedItem(String.valueOf(result));
        }
    }

    public DisUnitBean getValues() {
        Map<String, Object> one = unitBean.getOne();
        Set<Map.Entry<String, Integer>> entries = componentIndexs.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            one.put(entry.getKey(), Integer.valueOf((String) comboBoxes[entry.getValue()].getSelectedItem()));
        }
        unitBean.resolve(one);
        return unitBean;
    }


}
