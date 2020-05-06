package com.thingtek.socket.entity;

import javax.swing.*;

public class G2SSetFZ extends BaseG2S {
    @Override
    public void resolve() {
//        System.out.println(unitnum);
        super.resolve();
        JOptionPane.showMessageDialog(null, "单元 " + unitnum + " 设置阈值成功", "成功", JOptionPane.INFORMATION_MESSAGE);
    }
}
