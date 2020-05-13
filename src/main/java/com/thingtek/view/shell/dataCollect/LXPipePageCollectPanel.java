package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.beanServiceDao.unit.service.LXUnitService;
import com.thingtek.view.component.factory.Factorys;
import com.thingtek.view.component.listener.LXMouseMoveListenAdapter;
import com.thingtek.view.shell.BasePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LXPipePageCollectPanel extends BasePanel {

    public LXPipePageCollectPanel() {
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

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public LXPipePageCollectPanel init() {
        setLayout(new BorderLayout());
//        image = factorys.getIconFactory().getImage(getName());
        center = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                if (image != null) {
                    g.drawImage(image, 0, 0, center.getWidth(), center.getHeight(), center);
                }
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

    private boolean admin;

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

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

    public void refreshData() {
    }


    private void setButtonBounds() {
        for (LXUnitIconLabel button : buttonList) {
            LXUnitBean point = button.getUnitBean();
            button.setBounds((int) (point.getX() * center.getWidth()), (int) (point.getY() * center.getHeight()), 50, 50);
        }
    }

}
