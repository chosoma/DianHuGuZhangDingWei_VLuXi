package com.thingtek.view.component.factory;


import org.springframework.stereotype.Component;

import java.awt.Insets;
import java.util.Hashtable;
import java.util.Map;
@Component
public class MyInsetsFactory {

    private Map<String,Insets> insetsMap = new Hashtable<>();


    public static final Insets insets1011 = new Insets(1, 0, 1, 1);

    public static final Insets insets2233 = new Insets(2, 2, 3, 3);

}
