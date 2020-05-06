package com.thingtek.socket.entity;

import javax.swing.*;

public class G2SSetFDBS extends BaseG2S {
    @Override
    public void resolve() {
//        System.out.println(unitnum);
        super.resolve();
        JOptionPane.showMessageDialog(null, "单元 " + unitnum + " 设置放大倍数成功", "成功", JOptionPane.INFORMATION_MESSAGE);
    }
}
