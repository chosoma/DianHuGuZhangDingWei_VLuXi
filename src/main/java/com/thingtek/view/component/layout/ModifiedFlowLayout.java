package com.thingtek.view.component.layout;

import java.awt.*;

public class ModifiedFlowLayout extends FlowLayout {

    public ModifiedFlowLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    public Dimension minimumLayoutSize(Container target) {
        return computeSize(target, true);
    }

    public Dimension preferredLayoutSize(Container target) {
        return computeSize(target, false);
    }

    private Dimension computeSize(Container target, boolean isMinimum) {
        synchronized (target.getTreeLock()) {
            int hgap = getHgap();//横向间隔
            int vgap = getVgap();//纵向间隔
            Insets insets = target.getInsets();//便捷
            int maxWidth = target.getWidth() - insets.left - insets.right - hgap;//最大宽度
            int tempComponentHeigth = 0;// 单行最大高度
            Dimension dim = new Dimension(0, 0);
            int nmembers = target.getComponentCount();//个数
            boolean useBaseline = getAlignOnBaseline();//
            int maxAscent = 0;
            int maxDescent = 0;

            for (int i = 0; i < nmembers; i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = isMinimum ? m.getMinimumSize() : m.getPreferredSize();
                    if (dim.width + d.width + hgap > maxWidth) {
                        // 首个超大组件，尺寸宽度大于Container尺寸
                        if (tempComponentHeigth == 0) {
                            dim.height += d.height + vgap;
                        } else {
                            dim.height += tempComponentHeigth + vgap;
                            tempComponentHeigth = d.height;// 新行最大高度开始统计
                        }
                        dim.width = d.width + hgap;// 新航开始累计宽度
                    } else {
                        // 累计宽度
                        dim.width += d.width + hgap;
                        // 新行最大高度
                        tempComponentHeigth = Math.max(tempComponentHeigth,
                                d.height);
                    }

                    if (useBaseline) {
                        int baseline = m.getBaseline(d.width, d.height);
                        if (baseline >= 0) {
                            maxAscent = Math.max(maxAscent, baseline);
                            maxDescent = Math.max(maxDescent, d.height
                                    - baseline);
                        }
                    }
                }
            }
            // 如果不是唯一超大组件（唯一超大组件，组件尺寸大于Container）
            if (tempComponentHeigth != 0) {
                dim.height += tempComponentHeigth + vgap;// 最后一行组件高度
            }

            if (useBaseline) {
                dim.height = Math.max(maxAscent + maxDescent, dim.height);
            }
            dim.width += insets.left + insets.right + hgap;
            dim.height += insets.top + insets.bottom + vgap;

            return dim;
        }

    }
}
