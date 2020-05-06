package com.thingtek.start;

import com.thingtek.view.index.Index;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;

public class Start {
    public static void main(String[] args) {

        try {
            String config = "appcontext/applicationContext.xml";
            ApplicationContext ac = new ClassPathXmlApplicationContext(config);
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
            ac.getBean(Index.class).init();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
