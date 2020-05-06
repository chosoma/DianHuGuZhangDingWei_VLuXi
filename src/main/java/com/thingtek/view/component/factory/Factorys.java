package com.thingtek.view.component.factory;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public@Data
class Factorys {
    @Resource
    private MyAlphaCompositeFactory alphaCompositeFactory;
    @Resource
    private MyBorderFactory borderFactory;
    @Resource
    private MyColorFactory colorFactory;
    @Resource
    private MyFontFactory fontFactory;
    @Resource
    private MyIconFactory iconFactory;
    @Resource
    private MyInsetsFactory insetsFactory;
}
