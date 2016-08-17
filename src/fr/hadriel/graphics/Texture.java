package fr.hadriel.graphics;

import fr.hadriel.math.Vec2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by glathuiliere on 22/07/2016.
 */
public class Texture implements HLRenderable {

    private BufferedImage image;

    public Texture(BufferedImage image) {
        this.image = image;
    }

    public Texture(File file) throws IOException {
        this(ImageIO.read(file));
    }

    public Texture(String filename) throws IOException {
        this(new File(filename));
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    /**
     * @param x the x Offset of the region
     * @param y the y Offset of the region
     * @param width the width of the region
     * @param height the height of the region
     * @return a Texture instance representing the region queried
     */
    public Texture createTexture(int x, int y, int width, int height) {
        if(width == 0 || height == 0)
            return null;
        BufferedImage child = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = child.createGraphics();
        g.translate(-x, -y);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return new Texture(child);
    }

    public void render(HLGraphics g) {
        g.drawImage(image);
    }

    public void renderStretched(HLGraphics g, int width, int height) {
        g.drawImageStretched(image, width, height);
    }

    public void renderStretched(HLGraphics g, Vec2 size) {
        renderStretched(g, (int) size.x, (int) size.y);
    }
}