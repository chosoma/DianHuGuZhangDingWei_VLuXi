package com.thingtek.socket.entity;

import javax.swing.*;

public class G2SSearchMC extends BaseG2S implements UnUnitNum {
    @Override
    public void resolve() {
//        System.out.println(unitnum);
        byte mi = bytes[0];
        byte se = bytes[1];
        int mc = (mi & 0xff) * 60 + se;
        JOptionPane.showMessageDialog(null, "当前脉冲间隔:" + mc + "秒", "成功", JOptionPane.INFORMATION_MESSAGE);
    }
}
