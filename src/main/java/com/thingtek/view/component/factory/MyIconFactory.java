package com.thingtek.view.component.factory;

import lombok.Data;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public @Data
class MyIconFactory {


    public List<String> logoIconTexts;

    public List<Image> getLogoIcons() {
        List<Image> images = new ArrayList<>();
        for (String str : logoIconTexts) {
            images.add(new ImageIcon(this.getClass().getClassLoader().getResource(str)).getImage());
        }
        return images;
    }

    private Map<String, String> iconpathMap = new Hashtable<>();

    private Map<String, String[]> iconpathsMap = new Hashtable<>();


    public void setIconpathMap(Map<String, String> iconpathMap) {
        this.iconpathMap = iconpathMap;
    }


    public ImageIcon getIcon(String string) {
//        System.out.println(string+":"+iconpathMap.get(string));
        return new ImageIcon(this.getClass().getClassLoader().getResource(iconpathMap.get(string)));
    }

    public ImageIcon[] getIcons(String string) {
//        System.out.println(string+":"+iconpathMap.get(string));
        String[] iconTests = iconpathsMap.get(string);
        ImageIcon[] imageIcons = new ImageIcon[iconTests.length];
        imageIcons[0] = new ImageIcon(this.getClass().getClassLoader().getResource(iconTests[0]));
        imageIcons[1] = new ImageIcon(this.getClass().getClassLoader().getResource(iconTests[1]));
        return imageIcons;
    }

    public Image getImage(String string) {
//        System.out.println(string+":"+iconpathMap.get(string));
//        System.out.println(this.getClass().getClassLoader().getResource(iconpathMap.get(string)));
        try {
            return ImageIO.read(this.getClass().getClassLoader().getResource(iconpathMap.get(string)));
        } catch (Exception e) {
            return null;
        }
    }


}
