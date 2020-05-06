package com.thingtek.view.component.dialog;

import com.thingtek.view.component.dialog.base.BaseMenuDialog;

import javax.swing.*;
import java.awt.*;

public class SystemDialog extends BaseMenuDialog {

    public SystemDialog(JFrame jFrame, String titleText, Image icon) {
        super(jFrame, titleText, icon);
    }

    @Override
    public SystemDialog initDialog() {
        super.initDialog();
        return this;
    }

}

