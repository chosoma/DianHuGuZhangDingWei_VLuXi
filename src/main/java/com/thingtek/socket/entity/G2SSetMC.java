package com.thingtek.socket.entity;

import javax.swing.*;

public class G2SSetMC extends BaseG2S implements UnUnitNum {
    @Override
    public void resolve() {
//        System.out.println(unitnum);
        JOptionPane.showMessageDialog(null, "设置脉冲成功", "成功", JOptionPane.INFORMATION_MESSAGE);
    }
}
