package fr.hadriel.graphics.font;

import fr.hadriel.graphics.g2d.Graphics;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;

import java.awt.*;
import java.io.*;

/**
 * Created by glathuiliere on 13/02/2017.
 */
public class TrueTypeFont implements IFont {

    private static final float DEFAULT_MAX_RENDER_SIZE = 80f;
    private static final int DEFAULT_MIPMAP_COUNT = 4;

    private BitmapFont[] mipmaps;

    public TrueTypeFont(Font font, float maxRenderSize, int mipmapCount) {
        mipmaps = new BitmapFont[mipmapCount];
        float size = maxRenderSize;
        for(int i = 0; i < mipmapCount; i++) {
            System.out.println("Generating mipmap with size : " + size);
            Font mipmapFont = font.deriveFont(size);
            mipmaps[i] = new BitmapFont(mipmapFont);
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

    private BitmapFont getMipmapGLFont(float targetSize) {
        BitmapFont selected = mipmaps[0];

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

    public void drawText(Graphics g, String string, float size, Vec4 color) {
        getMipmapGLFont(size).drawText(g, string, size, color);
    }

    public Vec2 getSizeOfString(String text, float size) {
        return getMipmapGLFont(size).getSizeOfString(text, size);
    }
}