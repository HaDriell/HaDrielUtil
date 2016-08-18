package fr.hadriel.graphics;

import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.HLRenderable;
import fr.hadriel.graphics.Transform;
import fr.hadriel.graphics.ui.*;
import fr.hadriel.math.Vec2;

import java.awt.*;
import java.awt.font.GlyphVector;

/**
 * Created by glathuiliere on 11/08/2016.
 */
public class Text implements HLRenderable {

    private String text;
    private Font font;
    private float fontSize;
    private Color color;

    private Vec2 size;
    private Transform textTransform;

    public Text(String text, Font font, float fontSize, Color color) {
        this.text = text;
        this.font = font.deriveFont(100f);
        this.fontSize = fontSize;
        this.color = color;
        this.textTransform = new Transform();
        this.size = new Vec2();
        computeSizeAndTransform();
    }

    public Text(String text) {
        this(text, UIDefaults.DEFAULT_FONT, UIDefaults.DEFAULT_FONTSIZE, UIDefaults.DEFAULT_COLOR);
    }

    private void computeSizeAndTransform() {
        GlyphVector glyph = font.createGlyphVector(UIDefaults.HIGH_QUALITY_FONT_RENDER_CONTEXT, text);
        Rectangle r = glyph.getPixelBounds(UIDefaults.HIGH_QUALITY_FONT_RENDER_CONTEXT, 0, 0);
        float scale = fontSize / 100f;
        textTransform.setToTranslation(-r.x * scale, -r.y * scale);
        size.set(r.width, r.height).scale(scale, scale);
    }

    public void setText(String text) {
        this.text = text;
        computeSizeAndTransform();
    }

    public String getText() {
        return text;
    }

    public void setFont(Font font) {
        this.font = font == null ? UIDefaults.DEFAULT_FONT : font.deriveFont(100f);
        computeSizeAndTransform();
    }

    public Font getFont() {
        return font.deriveFont(fontSize);
    }

    public void setFontSize(float fontSize) {
        if(fontSize < 0)
            fontSize = 0;
        this.fontSize = fontSize;
        computeSizeAndTransform();
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setColor(Color color) {
        this.color = color == null ? UIDefaults.DEFAULT_COLOR : color;
    }

    public Color getColor() {
        return color;
    }

    public Vec2 getSize() {
        return size;
    }

    public void render(HLGraphics g) {
        g.pushTransform(textTransform.toMatrix());
        g.drawString(text, font, fontSize, color);
        g.popTransform();
    }
}