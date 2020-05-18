package com.thingtek.view.component.icon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class IconFactory {

    private static Icon CheckBoxIcon;
    private static Icon RadioButtonIcon;

    private static Icon OptionPaneErrorIcon;
    private static Icon OptionPaneInformationIcon;
    private static Icon OptionPaneQuestionIcon;
    private static Icon OptionPaneWarningIcon;

    private static Icon ReFreshIcon;

    private static Icon FoldIcon, VioceWarn, ShowDebug;


    public static Icon getFoldIcon() {
        if (FoldIcon == null) {
            FoldIcon = new FoldIcon();
        }
        return FoldIcon;
    }

    public static Icon getCheckBoxIcon() {
        if (CheckBoxIcon == null) {
            CheckBoxIcon = new CheckBoxIcon();
        }
        return CheckBoxIcon;
    }

    public static Icon getRadioButtonIcon() {
        if (RadioButtonIcon == null) {
            RadioButtonIcon = new RadioButtonIcon();
        }
        return RadioButtonIcon;
    }

    public static Icon getOptionPaneErrorIcon() {
        if (OptionPaneErrorIcon == null) {
            OptionPaneErrorIcon = new ImageIcon(
                    IconFactory.class.getClassLoader().getResource("icons/error.png"));
        }
        return OptionPaneErrorIcon;
    }

    public static Icon getOptionPaneQuestionIcon() {
        if (OptionPaneQuestionIcon == null) {
            OptionPaneQuestionIcon = new ImageIcon(
                    IconFactory.class.getClassLoader().getResource("icons/question.png"));
        }
        return OptionPaneQuestionIcon;
    }

    public static Icon getOptionPaneInformationIcon() {
        if (OptionPaneInformationIcon == null) {
            OptionPaneInformationIcon = new ImageIcon(
                    IconFactory.class.getClassLoader().getResource("icons/info.png"));
        }
        return OptionPaneInformationIcon;
    }

    public static Icon getOptionPaneWarningIcon() {
        if (OptionPaneWarningIcon == null) {
            OptionPaneWarningIcon = new ImageIcon(
                    IconFactory.class.getClassLoader().getResource("icons/warning.png"));
        }
        return OptionPaneWarningIcon;
    }

    public static Icon getReFreshIcon() {
        if (ReFreshIcon == null) {
            ReFreshIcon = new ImageIcon("images/refresh.png");
        }
        return ReFreshIcon;
    }

    public static Icon getVoiceWarnIcon() {
        if (VioceWarn == null) {
            VioceWarn = new VioceWarnIcon();
        }
        return VioceWarn;
    }


    public static Icon getShowDebugIcon() {
        if (ShowDebug == null) {
            ShowDebug = new ShowDebugIcon();
        }
        return ShowDebug;
    }

    public static Image sound_16, sound_muted_16, showDebug_16, showDebug_muted_16, warn, warn_28;

    static {
        try {
            sound_16 = ImageIO.read(IconFactory.class.getClassLoader().getResource("icons/sound_16.png"));
            sound_muted_16 = ImageIO.read(IconFactory.class.getClassLoader().getResource("icons/sound_muted_16.png"));
            showDebug_16 = ImageIO.read(IconFactory.class.getClassLoader().getResource("icons/showDebug_16.png"));
            showDebug_muted_16 = ImageIO.read(IconFactory.class.getClassLoader().getResource("icons/showDebug_muted_16.png"));
            warn = ImageIO.read(IconFactory.class.getClassLoader().getResource("icons/warn.png"));
            warn_28 = ImageIO.read(IconFactory.class.getClassLoader().getResource("icons/warn_28.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
