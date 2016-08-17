package fr.hadriel.graphics.ui;

import fr.hadriel.graphics.NinePatch;
import fr.hadriel.graphics.Texture;
import fr.hadriel.util.Callback;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Created by glathuiliere on 11/08/2016.
 */
public final class UIDefaults {
    private UIDefaults(){}

    public static BufferedImage loadImageInternal(String path) {
        try {
            return ImageIO.read(UIDefaults.class.getResourceAsStream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Font loadFontInternal(int format, String path) {
        try {
            return Font.createFont(format, UIDefaults.class.getResourceAsStream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage copyOf(BufferedImage image, Callback<BufferedImage> imageCallback) {
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = image.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        BufferedImage copy = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        if(imageCallback != null) imageCallback.execute(copy);
        return copy;
    }

    //Embedded resources:
    private static final Font BERYLIUM_REGULAR = loadFontInternal(Font.TRUETYPE_FONT, "/defaults/berylium rg.ttf");

    private static final BufferedImage BACKGROUND = loadImageInternal("/defaults/background.png");
    private static final BufferedImage CORNER = loadImageInternal("/defaults/corner.png");
    private static final BufferedImage SLIDER_BUTTON = loadImageInternal("/defaults/sliderButton.png");
    private static final BufferedImage SLIDER_BACKGROUND = loadImageInternal("/defaults/sliderBackground.png");

    // UI DEFAULT RESOURCES
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Text
    public static final Font DEFAULT_FONT = BERYLIUM_REGULAR;
    public static final FontRenderContext HIGH_QUALITY_FONT_RENDER_CONTEXT = new FontRenderContext(null, true, true);
    public static final float DEFAULT_FONTSIZE = 20f;
    public static final Color DEFAULT_COLOR = Color.white;

    //Button
    public static final NinePatch DEFAULT_IDLE_PATCH = new NinePatch(BACKGROUND, 8, 8, 8, 8);
    public static final NinePatch DEFAULT_HOVERED_PATCH = new NinePatch(copyOf(BACKGROUND, (image) -> {
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(255, 255, 255, 15)); // change alpha to change lighting ?
        g.fillRect(8, 8, image.getWidth() - 9, image.getHeight() - 9);
        g.dispose();
    }), 8, 8, 8, 8);
    public static final NinePatch DEFAULT_PRESSED_PATCH = new NinePatch(copyOf(BACKGROUND, (image) -> {
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(0, 0, 0, 15)); // change alpha to change lighting ?
        g.fillRect(8, 8, image.getWidth() - 9, image.getHeight() - 9);
        g.dispose();
    }), 8, 8, 8, 8);

    //Slider
    public static final NinePatch DEFAULT_SLIDER_BACKGROUND_PATCH = new NinePatch(SLIDER_BACKGROUND, 3, 10, 3, 10);
    public static final NinePatch DEFAULT_SLIDER_BUTTON_PATCH = new NinePatch(SLIDER_BUTTON, 0, 0, 0, 0);
}
