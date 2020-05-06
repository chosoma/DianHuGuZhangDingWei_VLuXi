package com.thingtek.view.shell.dataCollect;

import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.view.component.button.CollectTitleButton;
import com.thingtek.view.component.tablecellrander.TCR;
import com.thingtek.view.component.tablemodel.CollectWarnTableModel;
import com.thingtek.view.logo.LogoInfo;
import com.thingtek.view.shell.BasePanel;
import com.thingtek.view.shell.DataPanel;
import com.thingtek.view.shell.dataCollect.base.BaseCollectPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;

/**
 * 数据采集
 */

public class DataCollectPanel extends BasePanel implements DataPanel {
    @Resource
    private LogoInfo logoInfo;

    private Map<Integer, String> warntitlemap;

    public void setWarntitlemap(Map<Integer, String> warntitlemap) {
        this.warntitlemap = warntitlemap;
    }

    @Override
    public DataCollectPanel init() {
        setLayout(new BorderLayout());
        setOpaque(false);
        initLeft();
        initCenter();
        initCompnent();
        initBottom();
        refreashData();
//        refreashWarn();
        return this;
    }

    private @Resource
    CollectWarnTableModel tableModel;

    private JTable warntable;

    private void initBottom() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        warntable = new JTable(tableModel);
        JScrollPane jScrollPane = new JScrollPane(warntable);
        bottomPanel.add(jScrollPane, BorderLayout.CENTER);
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 100));
        this.add(bottomPanel, BorderLayout.SOUTH);
        /*java.util.List<WarnBean> warnBeanList = warnService.getLastWarn();
        java.util.List<Vector<Object>> vectors = new ArrayList<>();
        for (WarnBean warn : warnBeanList) {
            vectors.add(warn.getTableCol());
        }
        tableModel.addDatas(vectors);*/
        initializeTable();
    }

    @Resource
    private TCR tcr;

    private void initializeTable() {
        tcr.initializeTable(warntable);
    }

    private void initCompnent() {
        Set<Map.Entry<String, BaseCollectPanel>> entrys = logoInfo.getCollectPanelMap().entrySet();
        for (Map.Entry<String, BaseCollectPanel> entry : entrys) {
            final String name = entry.getKey();
            final BaseCollectPanel clt = entry.getValue();
            final CollectTitleButton ctb = new CollectTitleButton(name, factorys.getIconFactory().getIcon(name));
            ctb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    myButtonGroup(ctb, name);
                }
            });
            ctb.setSelected(clt.isDefaultselect());
            buttonPanel.add(ctb);
            clt.init();
            center.add(clt, name);
        }
    }

    private CardLayout card;
    private JPanel center;

    private void initCenter() {
        card = new CardLayout();
        center = new JPanel(card);
        add(center, BorderLayout.CENTER);
    }

    private JPanel buttonPanel;

    private void initLeft() {
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.white);
        buttonPanel.setPreferredSize(new Dimension(90, getWidth()));
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setOpaque(false);
        this.add(buttonPanel, BorderLayout.WEST);
    }

    private void myButtonGroup(CollectTitleButton jb, String name) {
        if (!jb.isSelected()) {
            jb.setSelected(true);
            for (Component b : buttonPanel.getComponents()) {
                if (b != jb && ((CollectTitleButton) b).isSelected()) {
                    ((CollectTitleButton) b).setSelected(false);
                }
            }
        }
        card.show(center, name);
    }

    public void addtablewarn(WarnBean warnBean) {
        tableModel.addData(warnBean.getCollectTableCol());
    }


    @Override
    public void refreashData() {
        for (BaseCollectPanel collectPanel : logoInfo.getCollectPanelMap().values()) {
            collectPanel.refreshData();
        }
    }

    public void showWarn(int clttype) {
        String title = warntitlemap.get(clttype);
        for (Component component : buttonPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().equals(title)) {
                    button.doClick();
                }
            }
        }

    }
}
