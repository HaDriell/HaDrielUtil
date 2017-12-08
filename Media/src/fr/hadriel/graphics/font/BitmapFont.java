package fr.hadriel.graphics.font;

import fr.hadriel.graphics.g2d.Graphics;
import fr.hadriel.graphics.io.ImageData;
import fr.hadriel.graphics.opengl.Texture;
import fr.hadriel.graphics.opengl.TextureHint;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by glathuiliere on 06/04/2017.
 */
public class BitmapFont implements IFont {

    private static final int FONT_TEXTURE_SIZE = 2048; //Maximum OpenGL standard Texture size support

    private final Texture texture;
    private final Map<Integer, TTFChar> cmap;
    private final float lineheight;
    private final int renderSize;

    public BitmapFont(Font font) {
        this.cmap = new HashMap<>();
        this.renderSize = font.getSize();
        BufferedImage bufferedImage = new BufferedImage(FONT_TEXTURE_SIZE, FONT_TEXTURE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setColor(Color.white);

        FontMetrics metrics = g.getFontMetrics();
        lineheight = metrics.getAscent() + metrics.getDescent();
        AffineTransform transform = new AffineTransform();
        float offsetX = 0;
        float offsetY = lineheight;
        for(int c = 31; c < 256; c++) {
            transform.setToTranslation(offsetX, offsetY);
            g.setTransform(transform);
            TextLayout layout = new TextLayout("" + (char) c, font, g.getFontRenderContext());
            layout.draw(g, 0, 0);
            TTFChar ttfc = new TTFChar(
                    offsetX,
                    offsetY - layout.getAscent(),
                    layout.getAdvance(),
                    layout.getAdvance(),
                    lineheight);
            cmap.put(c, ttfc);

            offsetX += layout.getAdvance() + 4; // 4 pixels spacing for the render safety
            if(offsetX + layout.getAdvance() > FONT_TEXTURE_SIZE) {
                offsetX = 0;
                offsetY += lineheight + 4; // 4 pixels spacing for the render safety
            }
        }
        g.dispose();

        //OpenGL Texture loading
        TextureHint hint = new TextureHint();
        hint.GL_MIN_FILTER = GL11.GL_LINEAR_MIPMAP_LINEAR;
        ImageData imageData = new ImageData(bufferedImage.getRGB(0, 0, FONT_TEXTURE_SIZE, FONT_TEXTURE_SIZE, null, 0, FONT_TEXTURE_SIZE),
                FONT_TEXTURE_SIZE,
                FONT_TEXTURE_SIZE,
                4);
        texture = new Texture(imageData, hint);
    }

    public float getLineheight() {
        return lineheight;
    }

    public int getRenderSize() {
        return renderSize;
    }

    public void drawText(Graphics g, String string, float targetSize, Vec4 color) {
        float offsetX = 0;
        float scale = targetSize / renderSize;
        Matrix3f matrix = new Matrix3f();
        for (int i = 0; i < string.length(); i++) {
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

    public Vec2 getSizeOfString(String text, float targetSize) {
        float scale = targetSize / renderSize;
        float advance = 0;
        for(int i = 0; i < text.length(); i++) {
            advance += getTTFChar(text.charAt(i)).advance;
        }
        return new Vec2(advance, lineheight).scale(scale, scale);
    }
}