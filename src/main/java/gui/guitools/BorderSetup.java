package gui.guitools;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

import static database.Constants.formColor;

public class BorderSetup {
    public static Border outerBorder(String title) {
        return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(formColor, 3), title);
    }

    public static Border outerBorder(String title, Font font, Color color) {
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(color, 3), title);
        border.setTitleFont(font);
        return border;
    }

    public static Border outerBorder(String title, boolean toTheRight) {
        TitledBorder out = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(formColor, 3), title);
        if (toTheRight) {
            out.setTitleJustification(TitledBorder.RIGHT);
        }
        return out;
    }

    public static Border innerBorder(int[] insets) {
        return BorderFactory.createEmptyBorder(insets[0], insets[1], insets[2], insets[3]);
    }

    public static Border totalBorder(String title, int[] insets) {
        return BorderFactory.createCompoundBorder(innerBorder(insets), outerBorder(title));
    }

    public static Border totalBorder(String title, int[] insets, boolean toTheRight) {
        return BorderFactory.createCompoundBorder(innerBorder(insets), outerBorder(title, toTheRight));
    }

    public static Border totalBorder(String title, int[] insets, Font font) {
        return BorderFactory.createCompoundBorder(innerBorder(insets), outerBorder(title, font, formColor));
    }

    public static Border totalBorder(String title, int[] insets, Font font, Color color) {
        return BorderFactory.createCompoundBorder(innerBorder(insets), outerBorder(title, font, color));
    }
}
