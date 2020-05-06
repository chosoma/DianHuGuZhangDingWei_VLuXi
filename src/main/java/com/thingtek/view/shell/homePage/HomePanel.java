package com.thingtek.view.shell.homePage;

//import com.thingtek.view.shell.warningManage.LinePanel;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends GlassPanel {


    private String logoinfo;

    public void setLogoinfo(String logoinfo) {
        this.logoinfo = logoinfo;
    }

    public HomePanel init() {
        super.init();
        this.setLayout(new BorderLayout());

        JPanel center = new JPanel(null);
        center.setOpaque(false);
        center.setLayout(new BorderLayout());
        this.add(center, BorderLayout.CENTER);

        JPanel bottom = new GlassPanel(new BorderLayout(), 0.3f);
        JLabel companyName = new JLabel(logoinfo, JLabel.CENTER);
        companyName.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        bottom.add(companyName, BorderLayout.NORTH);
//        linePanel = new LinePanel();
//        center.add(linePanel,BorderLayout.CENTER);

        /*JButton jButton = new JButton("test");
        jButton.setBounds(0, 0, 50, 50);
        MouseMoveListenAdapter mouseMoveListenAdapter = new MouseMoveListenAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point point = center.getMousePosition();
                jButton.setBounds((int) point.getX() - getX(), (int) point.getY() - getY(), jButton.getWidth(), jButton.getHeight());
            }
        };
        jButton.addMouseListener(mouseMoveListenAdapter);
        jButton.addMouseMotionListener(mouseMoveListenAdapter);
        new MouseMoveListenAdapter(jButton,center);
        center.add(jButton);*/


//        this.add(bottom, BorderLayout.SOUTH);
        return this;
    }
//    private LinePanel linePanel;
//    public void addData(DisDataBean data) {
//        linePanel.addData(data);
//    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}