package com.thingtek.view.component.combobox;

import javax.swing.*;

public class PhaseComboBox extends JComboBox {

    public PhaseComboBox() {
        super();
        String[] phases = new String[]{
          "A","B","C"
        };
        for (int i = 0; i < phases.length; i++) {
            this.addItem(phases[i]);
        }
    }
}
