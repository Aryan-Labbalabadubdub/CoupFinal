package gui.guitools;

import javax.swing.*;
import java.awt.*;

import static gui.guitools.Convert.dimensionConverter;
import static gui.guitools.Convert.fadeImage;

public class PanelB extends JPanel {
    private static final boolean rainbow = false;
    private Image currentPic;
    private float[] dim;

    public PanelB(Image img, float[] percentages) {
        this.setCurrentPic(img);
        this.setDim(percentages);
        this.setOpaque(false);
    }

    public void fadeBackground(float opacity) {
        setCurrentPic(fadeImage(getCurrentPic(), opacity));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int initialX = (int) dimensionConverter(getDim()[0], getDim()[1]).getWidth();
        int initialY = (int) dimensionConverter(getDim()[0], getDim()[1]).getHeight();
        int sizeX = (int) dimensionConverter(getDim()[2], getDim()[3]).getWidth();
        int sizeY = (int) dimensionConverter(getDim()[2], getDim()[3]).getHeight();
        g.drawImage(getCurrentPic(), initialX, initialY, sizeX, sizeY, null);
    }

    public Image getCurrentPic() {
        return currentPic;
    }

    public void setCurrentPic(Image currentPic) {
        this.currentPic = currentPic;
    }

    public float[] getDim() {
        return dim;
    }

    public void setDim(float[] dim) {
        this.dim = dim;
    }

}
