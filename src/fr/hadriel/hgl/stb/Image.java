package fr.hadriel.hgl.stb;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.*;

/**
 * Created by glathuiliere on 29/11/2016.
 */
public class Image {
    public final ByteBuffer pixels;
    public final int width;
    public final int height;
    public final int components;

    Image(ByteBuffer pixels, int width, int height, int components) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.components = components;
    }

    public static Image load(String filename) {
        int[] x = new int[1];
        int[] y = new int[1];
        int[] components = new int[1];
        ByteBuffer pixels = stbi_load(filename, x, y, components, STBI_rgb_alpha);
        return pixels == null ? null : new Image(pixels, x[0], y[0], components[0]);
    }

    public static void unload(Image image) {
        stbi_image_free(image.pixels);
    }
}