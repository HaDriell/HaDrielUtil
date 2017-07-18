package fr.hadriel.main.lwjgl.font;

import fr.hadriel.main.lwjgl.g2d.BatchGraphics;
import fr.hadriel.main.math.Vec2;
import fr.hadriel.main.math.Vec4;

import java.awt.*;
import java.io.*;

/**
 * Created by glathuiliere on 13/02/2017.
 */
public class TrueTypeFont {

    private static final float DEFAULT_MAX_RENDER_SIZE = 80f;
    private static final int DEFAULT_MIPMAP_COUNT = 4;

    private GLFont[] mipmaps;

    public TrueTypeFont(Font font, float maxRenderSize, int mipmapCount) {
        mipmaps = new GLFont[mipmapCount];
        float size = maxRenderSize;
        for(int i = 0; i < mipmapCount; i++) {
            System.out.println("Generating mipmap with size : " + size);
            Font mipmapFont = font.deriveFont(size);
            mipmaps[i] = new GLFont(mipmapFont);
            size /= 2;
        }
    }

    public TrueTypeFont(InputStream inputStream, float maxRenderSize, int mipmapCount) throws FontFormatException, IOException {
        this(Font.createFont(Font.TRUETYPE_FONT, inputStream), maxRenderSize, mipmapCount);
    }

    public TrueTypeFont(File file, float maxRenderSize, int mipmapCount) throws FontFormatException, IOException {
        this(new FileInputStream(file), maxRenderSize, mipmapCount);
    }

    public TrueTypeFont(String filename, float maxRenderSize, int mipmapCount) throws FontFormatException, IOException {
        this(new File(filename), maxRenderSize, mipmapCount);
    }

    private GLFont getMipmapGLFont(float targetSize) {
        GLFont selected = mipmaps[0];

        //In case we're magnifying
        if(selected.getRenderSize() < targetSize) return
         selected;

        for(int i = 1; i < mipmaps.length; i++) {
            if(selected.getRenderSize() > targetSize)
                break;
            selected = mipmaps[i];
        }
        return selected;
    }

    public void drawText(BatchGraphics g, String string, float size, Vec4 color) {
        getMipmapGLFont(size).drawText(g, string, size, color);
    }

    public Vec2 getSizeOfString(String text, float size) {
        return getMipmapGLFont(size).getSizeOfString(text, size);
    }
}