package fr.hadriel.graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere on 22/07/2016.
 */
public class TextureAtlas {

    private BufferedImage atlas;

    public TextureAtlas(BufferedImage atlas) {
        this.atlas = atlas;
    }

    public TextureAtlas(File file) throws IOException {
        this(ImageIO.read(file));
    }

    public TextureAtlas(String filename) throws IOException {
        this(new File(filename));
    }

    public int width() {
        return atlas.getWidth();
    }

    public int height() {
        return atlas.getHeight();
    }

    /**
     * @param x the x Offset of the region
     * @param y the y Offset of the region
     * @param width the width of the region
     * @param height the height of the region
     * @return a Texture instance representing the region queried
     */
    public BufferedImage getImage(int x, int y, int width, int height) {
        return atlas.getSubimage(x, y, width, height);
    }
}