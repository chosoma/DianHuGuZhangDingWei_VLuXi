package com.thingtek.view.component.dialog;

import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import com.thingtek.view.component.dialog.base.BaseSetDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class AddLXUnitDialog extends BaseSetDialog {

    public AddLXUnitDialog(JFrame jFrame, String titleText, Image icon) {
        super(jFrame, titleText, icon);
    }

    private JComboBox<Integer> jcbunitnum;

    private JTextField jtffz;

    private JComboBox<Integer> jcbpages;

    private JComboBox<String> jcbpipenames;

    private int clttype;

    public void setClttype(int clttype) {
        this.clttype = clttype;
    }

    @Override
    public AddLXUnitDialog initDialog() {
        super.initDialog();
        JLabel jlunitnum = new JLabel("单元编号:", JLabel.RIGHT);
        addLabel(jlunitnum);
        jcbunitnum = new JComboBox<>(unitService.getUnHasUnitNum(clttype));
        addInput(jcbunitnum);
        JLabel jlunitname = new JLabel("所属管体:", JLabel.RIGHT);
        addLabel(jlunitname);
        jcbpipenames = new JComboBox<>(pipeService.getPipeNames());
        jcbpipenames.setSelectedItem(null);
        jcbpipenames.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                PipeBean pipe = pipeService.getPipeByName((String) e.getItem());
                if (pipe == null) {
                    return;
                }
                Vector<Integer> vector = new Vector<>();
                for (int i = 1; i <= pipe.getPipe_page(); i++) {
                    vector.add(i);
                }
                jcbpages.setModel(new DefaultComboBoxModel<>(vector));
            }
        });
        addInput(jcbpipenames);
        JLabel jlphase = new JLabel("管体段位置:", JLabel.RIGHT);
        addLabel(jlphase);
        jcbpages = new JComboBox<>();
        addInput(jcbpages);
        setTotalSize(3);
        buttonSave.setText("添加");
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BaseUnitBean unitBean = getUnit();
                    unitService.saveLXUnit(unitBean);
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case KeyEvent.VK_ENTER:
                        buttonSave.doClick();
                        break;
                }
            }
        });
        return this;
    }

    private BaseUnitBean getUnit() throws Exception {
        PipeBean pipe = pipeService.getPipeByName((String) jcbpipenames.getSelectedItem());
        DisUnitBean unit = new DisUnitBean();
        unit.setUnit_num((short) (int) (Integer) jcbunitnum.getSelectedItem());
        if (pipe == null) {
            jcbpipenames.setSelectedIndex(0);
            pipe = pipeService.getPipeByName((String) jcbpipenames.getSelectedItem());
        }
        unit.setPipe(pipe);
        unit.setPipe_id(pipe.getPipe_id());
        unit.setPipe_page((Integer) jcbpages.getSelectedItem());
        return unit;
    }

}
