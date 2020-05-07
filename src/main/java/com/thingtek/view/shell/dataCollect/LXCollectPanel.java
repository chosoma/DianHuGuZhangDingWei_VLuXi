package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import com.thingtek.view.component.listener.MouseMoveListenAdapter;
import com.thingtek.view.shell.dataCollect.base.BaseCollectPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;

public class LXCollectPanel extends BaseCollectPanel {

    public LXCollectPanel() {
        clttype = 4;
        buttonList = new ArrayList<>();
    }

    private java.util.List<ChartIconLabel> buttonList;

    private JPanel center;

    private Image image;

    @Override
    public LXCollectPanel init() {
        super.init();
        setLayout(new BorderLayout());
        image = factorys.getIconFactory().getImage("background");
        center = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(image, 0, 0, center.getWidth(), center.getHeight(), center);
                setShowBounds();
            }
        };
        JScrollPane jScrollPane = new JScrollPane(center);
//        center.setOpaque(false);
        add(jScrollPane, BorderLayout.CENTER);
        refreshPoint();
        center.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setButtonBounds();
            }
        });
        return this;
    }

    @Override
    public void refreshPoint() {
        center.removeAll();
        buttonList.clear();
        java.util.List<PointBean> points = pointService.getPoints(clttype);
        ImageIcon icon = factorys.getIconFactory().getIcon("hitch");
        for (final PointBean point : points) {
            final ChartIconLabel button = new ChartIconLabel(point.getPoint_name(), icon);
            button.setPointBean(point);
            buttonList.add(button);
            final DisABCDataPanel datapanel = new DisABCDataPanel();
            datapanel.setTitle(point.getPoint_name());
            button.setDataPanel(datapanel);
            MouseMoveListenAdapter mmla = new MouseMoveListenAdapter(button, center);
            if (admin) {
                button.addListeners(mmla);
            }
            mmla.setPointBean(point);
            mmla.setPointService(pointService);
            center.add(button);
            center.add(datapanel);

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
//                    System.out.println("enter");
                    datapanel.setVisible(true);
                    button.stopWarning();
                }

                @Override
                public void mouseExited(MouseEvent e) {
//                    System.out.println("exited");
                    datapanel.setVisible(false);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    datapanel.removeWarn();
                }
            });
        }
        setButtonBounds();
        center.validate();
        center.updateUI();
    }

    @Override
    public void addWarn(DisDataBean warnBean) {
        DisUnitBean unit = (DisUnitBean) warnBean.getUnit();
        if (unit == null) {
            return;
        }
        for (ChartIconLabel button : buttonList) {
            PointBean pointBean = button.getPointBean();
            if (Objects.equals(pointBean.getPoint_num(), unit.getPoint_num())) {
                button.startWarning();
                DisABCDataPanel dataPanel = (DisABCDataPanel) button.getDataPanel();
                dataPanel.startWarn(unit);
                return;
            }
        }
    }

    @Override
    public void refreshData() {    }



    private void setButtonBounds() {
        for (ChartIconLabel button : buttonList) {
            PointBean point = button.getPointBean();
            button.setBounds((int) (point.getX() * center.getWidth()), (int) (point.getY() * center.getHeight()), 50, 50);
        }
    }

    private void setShowBounds() {
        int width = center.getWidth();
        int height = center.getHeight();
        int panelwidth = 125;
        int panelheight = 100;
        for (ChartIconLabel button : buttonList) {
            int panelx = button.getX() + button.getWidth();
            int panely = button.getY() + button.getHeight();
            if (panelx + panelwidth > width && panely + panelheight > height) {
                panelx = button.getX() - panelwidth;
                panely = height - panelheight;
            } else if (panelx + panelwidth > width) {
                panelx = width - panelwidth;
            } else if (panely + panelheight > height) {
                panely = height - panelheight;
            }
            button.getDataPanel().setBounds(panelx, panely, panelwidth, panelheight);
        }
    }
}
