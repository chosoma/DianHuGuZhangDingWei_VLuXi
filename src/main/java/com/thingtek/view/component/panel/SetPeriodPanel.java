package com.thingtek.view.component.panel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public @Data
class SetPeriodPanel extends JPanel {

    java.util.List<SetPeriodConfig> configs;
    Map<SetPeriodConfig, JComponent> componentMap;

    public void init() {
        for (SetPeriodConfig config : configs) {
            try {
                JComponent component = (JComponent) this.getClass()
                        .getClassLoader()
                        .loadClass(config.getComponentClass())
                        .newInstance();

                componentMap.put(config, component);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

@Data
class SetPeriodConfig {
    private int x, y, weightx, weighty;
    private String[] strs;
    private String componentClass;


}