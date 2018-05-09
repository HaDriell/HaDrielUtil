package fr.hadriel.asset.graphics.ui.components;

import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.asset.graphics.ui.UIElement;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.renderers.BatchGraphics;

import java.util.Objects;

public class Label extends UIElement {

    private String text;
    private Font font;
    private float fontSize;
    private Vec4 color;

    public Label(String text, Font font, float fontSize) {
        this(text, font, fontSize, new Vec4(1, 1, 1, 1));
    }

    public Label(String text, Font font, float fontSize, Vec4 color) {
        this.text = Objects.requireNonNull(text);
        this.font = Objects.requireNonNull(font);
        this.fontSize = fontSize;
        this.color = Objects.requireNonNull(color);
        autoResize();
    }

    private void autoResize() {
        Vec2 size = font.sizeof(text, fontSize);
        setSize(size.x, size.y);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = Objects.requireNonNull(text);
        autoResize();
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = Objects.requireNonNull(font);
        autoResize();
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
        autoResize();
    }

    public Vec4 getColor() {
        return color;
    }

    public void setColor(Vec4 color) {
        this.color = Objects.requireNonNull(color);
    }

    protected void onRender(BatchGraphics graphics) {
        graphics.draw(0, 0, text, font, fontSize, color);
    }
}