package gui.guitools;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Convert {
    public static Dimension dimensionConverter(float percentW, float percentH) {
        return new Dimension(Math.round(percentW * Frame.screenSize.width / 100), Math.round(percentH * Frame.screenSize.height / 100));
    }

    public static Dimension dimensionConverter(float percentH) {
        return new Dimension(Math.round(percentH * Frame.screenSize.height / 100), Math.round(percentH * Frame.screenSize.height / 100));
    }

    public static Dimension dimensionConverter(float percentW, float ratio, boolean proportional) {
        if (proportional) {
            return new Dimension(Math.round(percentW * Frame.screenSize.width / 100), Math.round(ratio * percentW * Frame.screenSize.width / 100));
        } else {
            return dimensionConverter(percentW, ratio);
        }
    }

    public static BufferedImage iconResize(Image rawImage, float[] percentages, boolean proportional) {

        BufferedImage img = (BufferedImage) rawImage;
        int width, height;
        if (proportional) {
            width = dimensionConverter(percentages[0], percentages[1], true).width;
            height = dimensionConverter(percentages[0], percentages[1], true).height;
        } else {
            width = dimensionConverter(percentages[0], percentages[1], false).width;
            height = dimensionConverter(percentages[0], percentages[1], false).height;
        }

        int type = (img.getTransparency() == Transparency.OPAQUE) ?
                BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage out = img;
        int w, h;
        // Use multi-step technique: start with original size, then scale down in multiple passes with drawImage() until the target size is reached
        w = img.getWidth();
        h = img.getHeight();

        do {
            if (w > width) {
                w /= 1.6;
                if (w < width) {
                    w = width;
                }
            }

            if (h > height) {
                h /= 1.6;
                if (h < height) {
                    h = height;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(out, 0, 0, w, h, null);
            g2.dispose();

            out = tmp;
        }
        while (w != width || h != height);

        return out;
    }

    public static BufferedImage iconResize(String path, float[] percentages, boolean proportional) {
        BufferedImage newImg = null;
        try {
            newImg = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return iconResize(newImg, percentages, proportional);
    }

    public static Image fadeImage(Image image, float opacity) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity / 100));
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return (new ImageIcon(bufferedImage).getImage());
    }

    public static Dimension scaleSize(Dimension dimension, float scale) {
        return new Dimension((int) (dimension.width * scale), (int) (dimension.height * scale));
    }

    public static Point scalePoint(Point point, float scale) {
        return new Point((int) (point.x * scale), (int) (point.x * scale));
    }
}
