package fr.hadriel.graphics.image;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.*;

public class ImageFile {
    public final int[] pixels;
    public final int width;
    public final int height;
    public final int components;

    /**
     *
     * @param pixels RGBA Pixels array
     * @param width width of the image
     * @param height height of the image
     * @param components number of color components
     */
    public ImageFile(int[] pixels, int width, int height, int components) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.components = components;
    }

    public ImageFile(int[] pixels, int width, int height) {
        this(pixels, width, height, 4);
    }

    public ImageFile(ByteBuffer pixels, int width, int height, int components) {
        this.pixels = new int[width * height];
        for(int i = 0; i < this.pixels.length; i++) {
            this.pixels[i] = pixels.getInt();
        }
        this.width = width;
        this.height = height;
        this.components = components;
    }

    public ImageFile(String filename) {
        int[] w = new int[1]; //width
        int[] h = new int[1]; //height
        int[] c = new int[1]; //color components
        ByteBuffer buffer = stbi_load(filename, w, h, c, STBI_rgb_alpha);
        width = w[0];
        height = h[0];
        components = c[0];
        pixels = new int[width * height];
        for(int i = 0; i < pixels.length; i++) {
            pixels[i] = buffer.getInt();
        }
        stbi_image_free(buffer);
    }
}