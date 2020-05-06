package com.thingtek.view.login;


import com.thingtek.view.component.factory.Factorys;
import com.thingtek.view.shell.BasePanel;
import com.thingtek.view.shell.Shell;
import com.thingtek.view.component.panel.ShadowPanel;
import com.thingtek.view.logo.LogoInfo;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;

@org.springframework.stereotype.Component
public class Loading extends JFrame {

    @Resource
    private Factorys factorys;
    @Resource
    private Shell shell;
    @Resource
    private LogoInfo logoInfo;

    private JPanel jpBackground, jpLoad;
    private JLabel jlbPageInfo;
    private JProgressBar jpbProgress;
    private JLabel jlbProgress;
    // 登录、取消按钮
    private JButton jbtApply;
    // 用户输入区域
    private JTextField jtfUserName;
    // 密码框
    private JPasswordField jpfPSW;

    // 构造方法
    public Loading() {
    }

    public Loading init() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        icons = factorys.getIconFactory().getLogoIcons();

        JPanel contentPane = new JPanel(new BorderLayout());
//        contentPane.setBorder(BorderFactory.createLineBorder(new Color(44, 46, 54)));
        Image image = factorys.getIconFactory().getImage("load");
        jpBackground = new ShadowPanel(image, 1.0f);
        jpBackground.setLayout(null);
        contentPane.add(jpBackground, BorderLayout.CENTER);


        JLabel jlbSoftname = new JLabel(logoInfo.getSoftName(), JLabel.CENTER);
        jlbSoftname.setFont(factorys.getFontFactory().getFont("loginSoftnameFont"));
        jlbSoftname.setForeground(factorys.getColorFactory().getColor("loginSoftnameFore"));
        addBackGround(jlbSoftname, 10, 10, 380, 40);

        jlbPageInfo = new JLabel("正在加载...");
        jlbPageInfo.setFont(factorys.getFontFactory().getFont("loginPageInfoFont"));
        jlbPageInfo.setForeground(factorys.getColorFactory().getColor("loginPageInfoFore"));
        addBackGround(jlbPageInfo, 40, 75, 120, 22);

        JLabel jlbCopyright = new JLabel(logoInfo.getCopyrightName() + logoInfo.getCompanyName(), JLabel.CENTER);
        jlbCopyright.setFont(factorys.getFontFactory().getFont("loginCopyrightFont"));
        jlbCopyright.setForeground(factorys.getColorFactory().getColor("loginCopyrightFore"));
        addBackGround(jlbCopyright, 10, 250, 380, 20);

        this.setIconImages(icons);
        this.setTitle(logoInfo.getSoftName());
        // 去除JDialog边框
//        this.setUndecorated(true);
        // 透明
        // AWTUtilities.setWindowOpaque(this, false);
        // 设置JDialog尺寸为背景图片大小
        this.setSize(400, 300);
        // 设置窗体居中
        this.setLocationRelativeTo(null);
        this.setContentPane(contentPane);
        this.initLoad();
        this.setVisible(true);
        load();
        return this;
    }

    private void initLoad() {
        jpLoad = new JPanel(null);
        addBackGround(jpLoad, 80, 120, 240, 120);

        JLabel jlbBridge = new JLabel();
        addLoad(jlbBridge, 0, 0, 240, 80);

        jlbProgress = new JLabel();
        jlbProgress.setHorizontalAlignment(JLabel.CENTER);// 文字居中
        addLoad(jlbProgress, 0, 70, 240, 18);

        jpbProgress = new JProgressBar(0, 100);
        jpbProgress.setStringPainted(true);// 显示百分比字符
        jpbProgress.setIndeterminate(false); // 不确定的进度条
        jpbProgress.setBorderPainted(false);// 取消边框
        jpbProgress.setOpaque(false);// 设置透明背景
        addLoad(jpbProgress, 0, 90, 240, 12);
    }

    // 加载进度显示
    private void loading(int percent, String loading) {
        jpbProgress.setValue(percent);
        jlbProgress.setText(loading);
    }

    java.util.List<Image> icons;

    private void load() {

        shell.init();
        shell.setIconImages(icons);

        Map<String, BasePanel> basemap = logoInfo.getBasePanelMap();
        Map<String, Map<String, BaseSystemPanel>> sysmap = logoInfo.getSetPanelMap();
        int onepercent = 100 / (basemap.size() + sysmap.size());
        int percent = 0;
        Set<Map.Entry<String, BasePanel>> basemapEntries = basemap.entrySet();
        for (Map.Entry<String, BasePanel> entry : basemapEntries) {
            loading(percent, "Loading " + entry.getKey());
            percent += onepercent;
            shell.addToolItem(entry.getValue(), entry.getKey());
        }
        Set<Map.Entry<String, Map<String, BaseSystemPanel>>> sysmapEntries = sysmap.entrySet();
        for (Map.Entry<String, Map<String, BaseSystemPanel>> entry : sysmapEntries) {
            loading(percent, "Loading " + entry.getKey());
            percent += onepercent;
            shell.addSystemSetMenuItem(entry.getValue(), entry.getKey());
        }
//        shell.addHelp();
        dispose();
        shell.setVisible(true);
    }

    private void addBackGround(JComponent component, int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
        component.setOpaque(false);
        jpBackground.add(component);
    }

    private void addLoad(JComponent component, int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
        component.setForeground(factorys.getColorFactory().getColor("loginloadfore"));
        jpLoad.add(component);
    }

}
