package fr.hadriel.lwjgl.data;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.*;

/**
 * Created by glathuiliere on 29/11/2016.
 */
public class Image {
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
    public Image(int[] pixels, int width, int height, int components) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.components = components;
    }

    public Image(int[] pixels, int width, int height) {
        this(pixels, width, height, 4);
    }

    public Image(ByteBuffer pixels, int width, int height, int components) {
        this.pixels = new int[width * height];
        for(int i = 0; i < this.pixels.length; i++) {
            this.pixels[i] = pixels.getInt();
        }
        this.width = width;
        this.height = height;
        this.components = components;
    }

    public Image(String filename) throws IOException {
        int[] w = new int[1]; //width
        int[] h = new int[1]; //height
        int[] c = new int[1]; //color components
        ByteBuffer buffer = stbi_load(filename, w, h, c, STBI_rgb_alpha);
        if (buffer == null) throw new IOException("Could not load Image : " + stbi_failure_reason());
        width = w[0];
        height = h[0];
        components = c[0];
        pixels = new int[width * height];
        for(int i = 0; i < pixels.length; i++) {
            pixels[i] = buffer.getInt();
        }
        buffer.clear();
        stbi_image_free(buffer);
    }
}