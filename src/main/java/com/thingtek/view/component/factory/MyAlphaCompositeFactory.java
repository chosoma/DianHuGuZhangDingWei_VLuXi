package com.thingtek.view.component.factory;

import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class MyAlphaCompositeFactory {


    public static AlphaComposite AlphaComposite_100 = AlphaComposite.SrcOver;
    public static AlphaComposite AlphaComposite_50F = AlphaComposite.SrcOver
            .derive(0.5f);
    public static AlphaComposite AlphaComposite_30F = AlphaComposite.SrcOver
            .derive(0.3f);
    public static AlphaComposite AlphaComposite_20F = AlphaComposite.SrcOver
            .derive(0.2f);
    public static AlphaComposite AlphaComposite_8F = AlphaComposite.SrcOver
            .derive(0.08f);

}
