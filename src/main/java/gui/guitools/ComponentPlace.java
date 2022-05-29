package gui.guitools;

import java.awt.*;

public class ComponentPlace {
    public static void fontAndPlace(Container target, GridBagConstraints gbc, boolean isVertical, Component[] items, Font font) {
        if (isVertical) {
            for (Component item : items) {
                item.setFont(font);
                target.add(item, gbc);
                gbc.gridy++;
            }
        } else {
            for (Component item : items) {
                item.setFont(font);
                target.add(item, gbc);
                gbc.gridx++;
            }
        }
    }

    public static void fontAndPlace(Container target, GridBagConstraints gbc, boolean isVertical, Component[] items) {
        if (isVertical) {
            for (Component item : items) {
                target.add(item, gbc);
                gbc.gridy++;
            }
        } else {
            for (Component item : items) {
                target.add(item, gbc);
                gbc.gridx++;
            }
        }
    }

    public static void fontAndPlace(Container target, GridBagConstraints gbc, boolean isVertical, Component[] items, Font font, Dimension itemDim) {
        for (Component item : items) {
            item.setPreferredSize(itemDim);
            item.setMinimumSize(itemDim);
        }
        fontAndPlace(target, gbc, isVertical, items, font);
    }
}
