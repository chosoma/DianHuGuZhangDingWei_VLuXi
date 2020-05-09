package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.beanServiceDao.unit.service.LXUnitService;
import com.thingtek.view.component.factory.Factorys;
import com.thingtek.view.component.listener.LXMouseMoveListenAdapter;
import com.thingtek.view.shell.dataCollect.base.BaseCollectPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LXCollectPanel extends BaseCollectPanel {

    public LXCollectPanel() {
        buttonList = new ArrayList<>();
    }

    public void setUnitService(LXUnitService unitService) {
        this.unitService = unitService;
    }

    public java.util.List<LXUnitBean> units;

    public void setUnits(List<LXUnitBean> units) {
        this.units = units;
    }

    public void setFactorys(Factorys factorys) {
        this.factorys = factorys;
    }

    private java.util.List<LXUnitIconLabel> buttonList;

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
        ImageIcon icon = factorys.getIconFactory().getIcon("hitch");
        for (final LXUnitBean unit : units) {
            final LXUnitIconLabel button = new LXUnitIconLabel(String.valueOf(unit.getUnit_num()), icon);
            button.setUnitBean(unit);
            buttonList.add(button);
            LXMouseMoveListenAdapter mmla = new LXMouseMoveListenAdapter(button, center);
            if (admin) {
                button.addListeners(mmla);
            }
            mmla.setUnitBean(unit);
            mmla.setUnitService(unitService);
            center.add(button);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.stopWarning();
                }
            });
        }
        setButtonBounds();
        center.validate();
        center.updateUI();
    }

    @Override
    public void addWarn(DisDataBean warnBean) {
        LXUnitBean unit = warnBean.getUnit();
        if (unit == null) {
            return;
        }
        for (LXUnitIconLabel button : buttonList) {
            LXUnitBean unitBean = button.getUnitBean();
            if (Objects.equals(unitBean.getUnit_num(), unit.getUnit_num())) {
                button.startWarning();
                return;
            }
        }
    }

    @Override
    public void refreshData() {
    }


    private void setButtonBounds() {
        for (LXUnitIconLabel button : buttonList) {
            LXUnitBean point = button.getUnitBean();
            button.setBounds((int) (point.getX() * center.getWidth()), (int) (point.getY() * center.getHeight()), 50, 50);
        }
    }

    private void setShowBounds() {
        /*int width = center.getWidth();
        int height = center.getHeight();
        int panelwidth = 125;
        int panelheight = 100;
        for (LXUnitIconLabel button : buttonList) {
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
        }*/
    }
}
