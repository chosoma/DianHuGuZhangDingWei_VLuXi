package com.thingtek.view.component.factory;

import com.thingtek.beanServiceDao.base.BaseService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public @Data
class MyIconFactory extends BaseService {


    public List<String> logoIconTexts;

    public List<Image> getLogoIcons() {
        List<Image> images = new ArrayList<>();
        for (String str : logoIconTexts) {
            try {
                images.add(new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource(str))).getImage());
            } catch (Exception e) {
                log(e);
            }
        }
        return images;
    }

    private Map<String, String> iconpathMap = new Hashtable<>();

    private Map<String, String[]> iconpathsMap = new Hashtable<>();


    public void setIconpathMap(Map<String, String> iconpathMap) {
        this.iconpathMap = iconpathMap;
    }


    public ImageIcon getIcon(String string) {
        try {
            return new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource(iconpathMap.get(string))));
        } catch (Exception e) {
            log(e);
            return null;
        }
    }

    public Image getImage(String string) {
        try {
            return ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource(iconpathMap.get(string))));
        } catch (Exception e) {
            log(e);
            return null;
        }
    }


}
