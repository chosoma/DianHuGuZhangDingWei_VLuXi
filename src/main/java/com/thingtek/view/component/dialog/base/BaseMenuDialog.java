package com.thingtek.view.component.dialog.base;

import com.thingtek.view.component.button.ChangeButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public abstract class BaseMenuDialog extends BaseDialog {


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

    private CardLayout cardLayout;

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
        button.addActionListener(e -> {
            for (JButton button1 : collectTitleButtons) {
                if (button1 != e.getSource()) {
                    button1.setSelected(false);
                } else {
                    button1.setSelected(true);
                    cardLayout.show(center, button1.getText());
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
