package com.thingtek.view.shell;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.thingtek.view.component.button.ShellTitleButton;
import com.thingtek.view.component.dialog.SystemDialog;
import com.thingtek.view.component.factory.Factorys;
import com.thingtek.view.logo.LogoInfo;
import com.thingtek.view.shell.dataCollect.DataCollectPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;

@org.springframework.stereotype.Component
public class Shell extends JFrame {

    @Resource
    private LogoInfo logoInfo;

    @Resource
    private Factorys factorys;

    private JPanel normalpanel;
    private JPanel toolBar;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private CardLayout centerCard;// 卡片布局

    public Dimension shellDimension = new Dimension(1000, 600);
    public Dimension systemDimension = new Dimension(800, 400);

    public Shell init() {
//        collectlist = new ArrayList<>();
        this.initWindow();
        this.initMenu();
        this.initTop();
        this.initCenter();
        this.initBottom();
        return this;
    }

    private void initWindow() {

        this.setTitle(logoInfo.getSoftName());// 标题

//        this.setUndecorated(true);// 去除边框修饰
//        AWTUtilities.setWindowOpaque(this, false);// 设置透明

        this.setSize(shellDimension);
        this.setMinimumSize(new Dimension(800, 600));
        this.setLocationRelativeTo(null);
        CardLayout contentCard = new CardLayout();
        JPanel contentPane = new JPanel(contentCard);
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(44, 46, 54)));
        setContentPane(contentPane);

//        normalpanel = new BackGroundPanel(factorys.getIconFactory().getImage("databackground"));
        normalpanel = new JPanel(new BorderLayout());
        normalpanel.setBackground(factorys.getColorFactory().getColor("shellback"));

        contentPane.add(normalpanel, "normal");

        // 窗体关闭弹出对话框提示："确定"、"取消"
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(Shell.this, "确认关闭?", "关闭程序", JOptionPane.OK_CANCEL_OPTION);
                switch (option) {
                    case JOptionPane.OK_OPTION:
                        exitSys();
                        break;
                }
            }
        });
    }

//    private java.util.List<Collect> collectlist;

    private JMenuBar menuBar;

    private void initMenu() {
        menuBar = new JMenuBar();
        menuBar.setBorderPainted(false);
        this.setJMenuBar(menuBar);
    }

    public void addMenu(JMenuItem item) {
        JMenu menu = new JMenu(item.getName());
        menu.add(item);
        menuBar.add(menu);
    }

    private void initTop() {
        // 顶部面板：放置标题面板和功能面板
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        normalpanel.add(topPanel, BorderLayout.NORTH);

        // 功能面板，放置“主界面”、“数据采集”······
        JPanel funcionPanel = new JPanel(new BorderLayout());
        funcionPanel.setOpaque(false);
        topPanel.add(funcionPanel, BorderLayout.CENTER);
        toolBar = new JPanel();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 2));
        toolBar.setOpaque(false);
        funcionPanel.add(toolBar, BorderLayout.CENTER);
    }

    // 初始化中间面板
    private void initCenter() {
        centerCard = new CardLayout();
        centerPanel = new JPanel(centerCard);
        centerPanel.setOpaque(false);
        normalpanel.add(centerPanel, BorderLayout.CENTER);
    }

    private void initBottom() {
        bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setOpaque(false);

        JLabel companyName = new JLabel("  " + logoInfo.getCopyrightName() + logoInfo.getCompanyName() + "  ", JLabel.CENTER);
        companyName.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        companyName.setOpaque(false);
        bottomPanel.add(companyName, BorderLayout.EAST);

        JPanel jPanel = new JPanel();
        jPanel.setOpaque(false);
        bottomPanel.add(jPanel, BorderLayout.WEST);

        normalpanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * 退出程序
     */
    private void exitSys() {
        System.exit(0);
    }

    private java.util.List<JButton> titleButtons = new ArrayList<>();

    public void addSystemSetMenuItem(Map<String, BaseSystemPanel> compMap, final String text) {
        JMenu menu = new JMenu(text);
        menuBar.add(menu);
        final SystemDialog systemDialog = new SystemDialog(this, text, factorys.getIconFactory().getImage(text));
        systemDialog.setSize(systemDimension);
        Set<Map.Entry<String, BaseSystemPanel>> entries = compMap.entrySet();
        for (final Map.Entry<String, BaseSystemPanel> entry : entries) {
            final String title = entry.getKey();
            JMenuItem item = new JMenuItem(entry.getKey());
            final BaseSystemPanel panel = entry.getValue();
            panel.init();
            panel.setTitle(title);
            if (panel.isShow()) {
                menu.add(item);
                systemDialog.addItem(panel, title);
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        systemDialog.showItem(title);
                    }
                });
            }
//            panel.loadingData();
        }
    }

    public void addHelp() {
        JMenu menu = new JMenu("帮助");
        menuBar.add(menu);
        JMenuItem item = new JMenuItem("帮助");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Runtime.getRuntime().exec("cmd /c start " + "help.chm");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        menu.add(item);
    }

    public void addToolItem(final BasePanel component, final String text) {
        component.init();
        JButton title = new ShellTitleButton(text, factorys.getIconFactory().getIcon(text));
        title.setFont(factorys.getFontFactory().getFont("title"));
        title.setForeground(factorys.getColorFactory().getColor("titleButtonForeground"));
        title.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JButton b : titleButtons) {
                    if (e.getSource() != b) {
                        b.setSelected(false);
                    } else {
                        b.setSelected(true);
                        centerCard.show(centerPanel, text);
                    }
                }
            }
        });
        if(component.isSelect()){
            title.setSelected(true);
        }
        titleButtons.add(title);
        // 内容
        if (component.isShow()) {
            toolBar.add(title);
        }
        centerPanel.add(component, text);
//        if (component instanceof Collect) {
//            collectlist.add((Collect) component);
//        }
    }

    @Resource
    private DataCollectPanel dataCollectPanel;

    public void showWarn(int clttype) {
        for (JButton button : titleButtons) {
            if (button.getText().equals("数据采集")) {
                button.doClick();
            }
        }
        dataCollectPanel.showWarn(clttype);
    }

}
