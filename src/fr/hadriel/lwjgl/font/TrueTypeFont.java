package fr.hadriel.lwjgl.font;

import fr.hadriel.lwjgl.data.Image;
import fr.hadriel.lwjgl.g2d.BatchGraphics;
import fr.hadriel.lwjgl.opengl.Texture;
import fr.hadriel.lwjgl.opengl.TextureHint;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere on 13/02/2017.
 */
public class TrueTypeFont {

    //TODO : probable cool upgrade would be
    //TODO :    - Lazy loading
    //TODO :    - Reloading texture on resize (fixing font size)

    private static final int FONT_TEXTURE_SIZE = 2048; //Maximum OpenGL standard Texture size support
    private static final int CHAR_COUNT_PER_ROW = 16; //Maximum OpenGL standard Texture size support
    private static final float FONT_RENDER_SIZE = FONT_TEXTURE_SIZE / CHAR_COUNT_PER_ROW;

    private Texture texture;
    private Map<Integer, TTFChar> cmap;
    private float lineheight;

    public TrueTypeFont(InputStream inputStream) throws IOException {
        this.cmap = new HashMap<>();

        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(FONT_RENDER_SIZE);
            BufferedImage bufferedImage = new BufferedImage(FONT_TEXTURE_SIZE, FONT_TEXTURE_SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            g.setFont(font);
//            g.setColor(Color.black);
//            g.fillRect(0, 0, FONT_TEXTURE_SIZE, FONT_TEXTURE_SIZE);
            g.setColor(Color.white);

            FontMetrics metrics = g.getFontMetrics();
            lineheight = metrics.getAscent() + metrics.getDescent();

            AffineTransform transform = new AffineTransform();
            Vec2 offset = new Vec2(0, lineheight);
            for(int c = 31; c < 256; c++) {
                transform.setToTranslation(offset.x, offset.y);
                g.setTransform(transform);

                TextLayout layout = new TextLayout("" + (char) c, font, g.getFontRenderContext());
                layout.draw(g, 0, 0); //draw

                TTFChar ttfc = new TTFChar(
                        offset.x,
                        offset.y - layout.getAscent(),
                        layout.getAdvance(),
                        layout.getAdvance(),
                        lineheight);
                cmap.put(c, ttfc);

                // Advance Offset to the next position
                offset.x += layout.getAdvance() + 4; // 4px spacing to ensure scaling won't do artifacts
                if(offset.x + layout.getAdvance() > FONT_TEXTURE_SIZE) {
                    offset.x = 0;
                    offset.y += lineheight + 4; // 4px spacing to ensure scaling won't do artifacts
                }
            }
            g.dispose();

            //For Debug Purpose
//            ImageIO.write(bufferedImage, "PNG", new File("output.png"));
//            System.exit(0);
            //For Debug Purpose


            //Create the Texture
            TextureHint hint = new TextureHint();
            hint.GL_MIN_FILTER = GL11.GL_LINEAR_MIPMAP_LINEAR;
//            hint.GL_MIN_FILTER = GL11.GL_NEAREST_MIPMAP_LINEAR;
//            hint.GL_TEXTURE_MIPMAP_COUNT = 8;
            texture = new Texture(new Image(
                    bufferedImage.getRGB(0, 0, FONT_TEXTURE_SIZE, FONT_TEXTURE_SIZE, null, 0, FONT_TEXTURE_SIZE),
                    FONT_TEXTURE_SIZE,
                    FONT_TEXTURE_SIZE,
                    4), hint);
        } catch (FontFormatException exception) {
            throw new IOException(exception);
        }
    }

    public TrueTypeFont(File file) throws IOException {
        this(new FileInputStream(file));
    }

    public TrueTypeFont(String filename) throws IOException {
        this(new File(filename));
    }

    public void drawText(BatchGraphics g, String string, float size, Vec4 color) {
        float offsetX = 0;
        float scale = size / FONT_RENDER_SIZE;
        Matrix3f matrix = new Matrix3f();
        for(int i = 0; i < string.length(); i++) {
            TTFChar ttfc = getTTFChar(string.charAt(i));
            matrix.setToTransform(scale, scale, 0, offsetX, 0);
            g.push(matrix);
            g.drawTextureRegion(
                    0,
                    0,
                    ttfc.getRegion(texture),
                    color);
            g.pop();
            offsetX += ttfc.advance * scale;
        }
    }

    public TTFChar getTTFChar(int c) {
        TTFChar ttfc = cmap.get(c);
        return ttfc != null ? ttfc : cmap.get(31);
    }

    public Vec2 getSizeOfString(String text, float size) {
        float scale = size / FONT_RENDER_SIZE;
        float advance = 0;
        for(int i = 0; i < text.length(); i++) {
            advance += getTTFChar(text.charAt(i)).advance;
        }
        return new Vec2(advance, lineheight).scale(scale, scale);
    }
}