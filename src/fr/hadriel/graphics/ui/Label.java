package fr.hadriel.graphics.ui;

import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.math.Matrix3f;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

/**
 * Created by glathuiliere on 10/08/2016.
 */
public class Label extends Widget {
    private static final FontRenderContext HQ_FRC = new FontRenderContext(null, true, true);

    private String text;
    private Font font;
    private float fontSize;
    private Color color;

    private Matrix3f glyphTransform;
    private GlyphVector glyph;

    public Label(String text, Font font, float fontSize, Color color) {
        this.text = text;
        this.fontSize = fontSize;
        this.color = color;
        this.font = font.deriveFont(100f);
        this.glyphTransform = new Matrix3f();
        computeLabel();
    }

    private void computeLabel() {
        glyph = font.createGlyphVector(HQ_FRC, text);
        Rectangle r = glyph.getPixelBounds(HQ_FRC, 0, 0);
        System.out.println(r);
        float scale = fontSize / 100f;
        glyphTransform.setToTranslation(-r.x * scale, -r.y * scale);
        size.set(r.width, r.height).scale(scale, scale);
    }

    public void onRender(HLGraphics graphics) {
        graphics.push(glyphTransform);
        graphics.drawString(text, font, fontSize, color);
        graphics.pop();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        computeLabel();
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font.deriveFont(100f);
        computeLabel();
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
        computeLabel();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}