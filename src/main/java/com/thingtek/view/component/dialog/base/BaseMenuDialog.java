package com.thingtek.view.component.dialog.base;

import com.thingtek.view.component.button.ChangeButton;
import com.thingtek.view.shell.BasePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public abstract class BaseMenuDialog extends BaseDialog {


    protected java.util.List<Map<String, BasePanel>> panelList;

    public BaseMenuDialog(JFrame jFrame, String titleText, Image icon) {
        super(jFrame, titleText, icon);
        initDialog();
    }


    @Override
    public BaseMenuDialog initDialog() {
        super.initDialog();
        return this;
    }

    protected JPanel left, center; //left 左侧选择 center 右侧显示

    protected CardLayout cardLayout;

    @Override
    public void initCenter() {
        cardLayout = new CardLayout();
        center = new JPanel(cardLayout);
        container.add(center, BorderLayout.CENTER);
    }

    @Override
    public void initTool() {
        left = new JPanel(new FlowLayout());
        left.setBackground(Color.white);
        left.setPreferredSize(new Dimension(70, getHeight()));
        container.add(left, BorderLayout.WEST);
    }

    private java.util.List<JButton> collectTitleButtons = new ArrayList<>();

    public void addItem(Component component, String text) {
        JButton button = new ChangeButton(text);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JButton button : collectTitleButtons) {
                    if (button != e.getSource()) {
                        button.setSelected(false);
                    } else {
                        button.setSelected(true);
                        cardLayout.show(center, button.getText());
                    }
                }
            }
        });
        collectTitleButtons.add(button);
        left.add(button);
        center.add(component, text);
    }

    public void showItem(String text) {
        cardLayout.show(center, text);
        for (Component component : left.getComponents()) {
            if (component instanceof ChangeButton) {
                ChangeButton button = (ChangeButton) component;
                if (Objects.equals(button.getText(), text)) {
                    button.setSelected(true);
                } else {
                    button.setSelected(false);
                }
            }
        }
        if (!isVisible()) {
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }


}
