package com.thingtek.view.component.dialog;

import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import com.thingtek.view.component.dialog.base.BaseSetDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUnitDialog extends BaseSetDialog {

    public AddUnitDialog(JFrame jFrame, String titleText, Image icon) {
        super(jFrame, titleText, icon);
    }

    private JComboBox<Integer> jcbunitnum;

    private JTextField jtffz;

    private JComboBox<String> jcbphase;

    private JComboBox<String> jcbpointnames;

    private int clttype;

    public void setClttype(int clttype) {
        this.clttype = clttype;
    }

    @Override
    public AddUnitDialog initDialog() {
        super.initDialog();
        JLabel jlunitnum = new JLabel("单元编号:", JLabel.RIGHT);
        addLabel(jlunitnum);
        jcbunitnum = new JComboBox<>(unitService.getUnHasUnitNum(clttype));
        addInput(jcbunitnum);
        JLabel jlunitname = new JLabel("测点:", JLabel.RIGHT);
        addLabel(jlunitname);
        jcbpointnames = new JComboBox<>(pointService.getPointNames(clttype));
        addInput(jcbpointnames);
        JLabel jlphase = new JLabel("相位:", JLabel.RIGHT);
        addLabel(jlphase);
        jcbphase = new JComboBox<>(new String[]{"A", "B", "C"});
        addInput(jcbphase);
        /*JLabel jlfz = new JLabel("报警阈值:", JLabel.RIGHT);
        addLabel(jlfz);
        jtffz = new JTextField();
        addInput(jtffz);*/
        setTotalSize(3);
        buttonSave.setText("添加");
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BaseUnitBean unitBean = getUnit();
                    if (unitService.saveUnit(clttype, unitBean)) {
//                        successMessage("添加成功");
                        dispose();
                    } else {
                        falseMessage("添加失败");
                        dispose();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        return this;
    }

    private BaseUnitBean getUnit() throws Exception {
        PointBean point = pointService.getPointByName(clttype,(String) jcbpointnames.getSelectedItem());
        BaseUnitBean unit = (BaseUnitBean) this.getClass().getClassLoader().loadClass(clazzConfig.getUnitClazzName(clttype)).newInstance();
        unit.setUnit_num((short) (int) (Integer) jcbunitnum.getSelectedItem());
        unit.setPoint(point);
        unit.setPoint_num(point.getPoint_num());
//        unit.setFz(Short.parseShort(jtffz.getText()));
        unit.setPhase((String) jcbphase.getSelectedItem());
        return unit;
    }

}
