package fr.hadriel.renderers;

import fr.hadriel.graphics.font.Font;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec4;
import fr.hadriel.opengl.*;

public class FontRenderer {
    private static final int MAX_CHARACTERS = 10_000;
    private static final int MAX_ELEMENTS = MAX_CHARACTERS * 4;

    private VertexArray vao;
    private VertexBuffer vbo;
    private IndexBuffer ibo;

    //UNIFORMS

    private Matrix4f projection;

    private Font font;
    private float size;

    private Vec4 fillColor;
    private Vec4 outlineColor;
    private Vec4 backgroundColor;

    public FontRenderer(float left, float right, float top, float bottom) {
        this.projection = Matrix4f.Orthographic(left, right, top, bottom, -1, 1);
        this.font = null;
        this.size = 0f;
        this.fillColor = new Vec4(1, 1, 1, 1);
        this.outlineColor = new Vec4(1, 1, 1, 1);
        this.backgroundColor = new Vec4();
        this.vao = new VertexArray(MAX_ELEMENTS, null);
    }

    public void setBackgroundColor(Vec4 backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setFillColor(Vec4 fillColor) {
        this.fillColor = fillColor;
    }

    public void setOutlineColor(Vec4 outlineColor) {
        this.outlineColor = outlineColor;
    }

    public Vec4 getBackgroundColor() {
        return backgroundColor;
    }

    public Vec4 getFillColor() {
        return fillColor;
    }

    public Vec4 getOutlineColor() {
        return outlineColor;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    public void draw(String text) {
        if(text == null || text.isEmpty()) return; // no text to render
        if(font == null) return; // no configured font

        //TODO : build up a VAO to fill with all those quads
        //TODO : render the text using the configured font
    }
}