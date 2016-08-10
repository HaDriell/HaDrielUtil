package fr.hadriel.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by glathuiliere on 22/07/2016.
 */
public class SpriteSheet {

    private BufferedImage atlas;

    public SpriteSheet(BufferedImage atlas) {
        this.atlas = atlas;
    }

    public SpriteSheet(File file) throws IOException {
        this(ImageIO.read(file));
    }

    public SpriteSheet(String filename) throws IOException {
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