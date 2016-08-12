package fr.hadriel.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by glathuiliere on 12/08/2016.
 */
public class NinePatch {

    private static final int TOP_LEFT       = 0;
    private static final int TOP_CENTER     = 1;
    private static final int TOP_RIGHT      = 2;
    private static final int CENTER_LEFT    = 3;
    private static final int CENTER_CENTER  = 4;
    private static final int CENTER_RIGHT   = 5;
    private static final int BOTTOM_LEFT    = 6;
    private static final int BOTTOM_CENTER  = 7;
    private static final int BOTTOM_RIGHT   = 8;

    private Texture[] textures;
    private int top;
    private int left;
    private int bottom;
    private int right;

    public NinePatch(BufferedImage image, int top, int left, int bottom, int right) {
        this(new Texture(image), top, left, bottom, right);
    }

    public NinePatch(String filename, int top, int left, int bottom, int right) throws IOException {
        this(new Texture(filename), top, left, bottom, right);
    }

    public NinePatch(File file, int top, int left, int bottom, int right) throws IOException {
        this(new Texture(file), top, left, bottom, right);
    }

    public NinePatch(Texture source, int top, int left, int bottom, int right) {
        set(source, top, left, bottom, right);
    }

    public void set(Texture source, int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;

        int width = source.getWidth();
        int height = source.getHeight();

        textures = new Texture[9];
        textures[TOP_LEFT     ] = source.createTexture(getX(TOP_LEFT, width), getY(TOP_LEFT, height), getWidth(TOP_LEFT, width), getHeight(TOP_LEFT, height));
        textures[TOP_CENTER   ] = source.createTexture(getX(TOP_CENTER, width), getY(TOP_CENTER, height), getWidth(TOP_CENTER, width), getHeight(TOP_CENTER, height));
        textures[TOP_RIGHT    ] = source.createTexture(getX(TOP_RIGHT, width), getY(TOP_RIGHT, height), getWidth(TOP_RIGHT, width), getHeight(TOP_RIGHT, height));
        textures[CENTER_LEFT  ] = source.createTexture(getX(CENTER_LEFT, width), getY(CENTER_LEFT, height), getWidth(CENTER_LEFT, width), getHeight(CENTER_LEFT, height));
        textures[CENTER_CENTER] = source.createTexture(getX(CENTER_CENTER, width), getY(CENTER_CENTER, height), getWidth(CENTER_CENTER, width), getHeight(CENTER_CENTER, height));
        textures[CENTER_RIGHT ] = source.createTexture(getX(CENTER_RIGHT, width), getY(CENTER_RIGHT, height), getWidth(CENTER_RIGHT, width), getHeight(CENTER_RIGHT, height));
        textures[BOTTOM_LEFT  ] = source.createTexture(getX(BOTTOM_LEFT, width), getY(BOTTOM_LEFT, height), getWidth(BOTTOM_LEFT, width), getHeight(BOTTOM_LEFT, height));
        textures[BOTTOM_CENTER] = source.createTexture(getX(BOTTOM_CENTER, width), getY(BOTTOM_CENTER, height), getWidth(BOTTOM_CENTER, width), getHeight(BOTTOM_CENTER, height));
        textures[BOTTOM_RIGHT ] = source.createTexture(getX(BOTTOM_RIGHT, width), getY(BOTTOM_RIGHT, height), getWidth(BOTTOM_RIGHT, width), getHeight(BOTTOM_RIGHT, height));
    }

    private int getX(int texture, int width) {
        switch (texture) {
            case TOP_LEFT:
            case CENTER_LEFT:
            case BOTTOM_LEFT:
                return 0;
            case TOP_CENTER:
            case CENTER_CENTER:
            case BOTTOM_CENTER:
                return left;
            case TOP_RIGHT:
            case CENTER_RIGHT:
            case BOTTOM_RIGHT:
                return width - right;
            default:
                throw new IllegalArgumentException("Invalid Texture ID");
        }
    }

    private int getY(int texture, int height) {
        switch (texture) {
            case TOP_LEFT:
            case TOP_CENTER:
            case TOP_RIGHT:
                return 0;
            case CENTER_LEFT:
            case CENTER_CENTER:
            case CENTER_RIGHT:
                return top;
            case BOTTOM_RIGHT:
            case BOTTOM_CENTER:
            case BOTTOM_LEFT:
                return height - bottom;
            default:
                throw new IllegalArgumentException("Invalid Texture ID");
        }
    }

    private int getWidth(int texture, int width) {
        switch (texture) {
            case TOP_LEFT:
            case CENTER_LEFT:
            case BOTTOM_LEFT:
                return left;
            case TOP_CENTER:
            case CENTER_CENTER:
            case BOTTOM_CENTER:
                return width - (left + right);
            case TOP_RIGHT:
            case CENTER_RIGHT:
            case BOTTOM_RIGHT:
                return right;
            default:
                throw new IllegalArgumentException("Invalid Texture ID");
        }
    }

    private int getHeight(int texture, int height) {
        switch (texture) {
            case TOP_LEFT:
            case TOP_CENTER:
            case TOP_RIGHT:
                return top;
            case CENTER_LEFT:
            case CENTER_CENTER:
            case CENTER_RIGHT:
                return height - (top + bottom);
            case BOTTOM_RIGHT:
            case BOTTOM_CENTER:
            case BOTTOM_LEFT:
                return bottom;
            default:
                throw new IllegalArgumentException("Invalid Texture ID");
        }
    }

    public Texture createTexture(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        HLGraphics g = new HLGraphics(image.createGraphics());
        render(g, width, height);
        g.dispose();
        return new Texture(image);
    }

    public void render(HLGraphics g, int width, int height) {
        for(int x = 0; x < 3; x++) {
            for(int y = 0; y < 3; y++) {
                int textureID = x + y * 3;
                int dx = getX(textureID, width);
                int dy = getY(textureID, height);
                int dw = getWidth(textureID, width);
                int dh = getHeight(textureID, height);
                if(dx < 0 || dy < 0 || dw < 0 || dh < 0)
                    continue;
                g.translate(dx, dy);
                textures[textureID].renderStretched(g, dw, dh);
                g.translate(-dx, -dy);
            }
        }
    }

    public int getBottom() {
        return bottom;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }
}