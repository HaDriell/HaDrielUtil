package fr.hadriel.asset.graphics.image;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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

    public ImageFile(ByteBuffer fileContent) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);
            ByteBuffer buffer = stbi_load_from_memory(fileContent, w, h, c, STBI_rgb_alpha);
            width = w.get(0);
            height = h.get(0);
            components = c.get(0);
            pixels = new int[width * height];
            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = buffer.getInt();
            }
            buffer.clear();
            stbi_image_free(buffer);
        }
    }

    public ImageFile(String filename) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);
            ByteBuffer buffer = stbi_load(filename, w, h, c, STBI_rgb_alpha);
            width = w.get(0);
            height = h.get(0);
            components = c.get(0);
            pixels = new int[width * height];
            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = buffer.getInt();
            }
            buffer.clear();
            stbi_image_free(buffer);
        }
    }
}