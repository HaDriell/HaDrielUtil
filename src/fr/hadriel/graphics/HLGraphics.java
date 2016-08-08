package fr.hadriel.graphics;

import fr.hadriel.math.Matrix3f;

import java.awt.*;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class HLGraphics {

    private Graphics2D g;
    private MatrixStack stack;

    public HLGraphics(Graphics2D g) {
        this.g = g;
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        this.stack = new MatrixStack();
    }

    public void push(Matrix3f matrix) {
        stack.push(matrix);
        g.setTransform(stack.top().toAffineTransform());
    }

    public void pop() {
        stack.pop();
        g.setTransform(stack.top().toAffineTransform());
    }

    public void showStackInfo() {
        System.out.println(stack.top());
    }

    public void setToIdentity() {
        stack.clear();
        g.setTransform(stack.top().toAffineTransform());
    }

    public void translate(float x, float y) {
        stack.top().translate(x, y);
        g.setTransform(stack.top().toAffineTransform());
    }

    public void rotate(float angle) {
        stack.top().rotate(angle);
        g.setTransform(stack.top().toAffineTransform());
    }

    public void scale(float x, float y) {
        stack.top().scale(x, y);
        g.setTransform(stack.top().toAffineTransform());
    }

    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }

    public void drawOval(int x, int y, int width, int height, Color color) {
        g.setColor(color);
        g.drawOval(x, y, width, height);
    }

    public void fillOval(int x, int y, int width, int height, Color color) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }

    public void drawRect(int x, int y, int width, int height, Color color) {
        g.setColor(color);
        g.drawRect(x, y, width, height);
    }

    public void fillRect(int x, int y, int width, int height, Color color) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public void drawString(String str, Font font, float size, Color color) {
        if(font.getSize() != 100)
            font = font.deriveFont(100f);
        Shape shape = font.layoutGlyphVector(g.getFontRenderContext(), str.toCharArray(), 0, str.length(), 0).getOutline();
        g.setColor(color);
        scale(size / 100f, size / 100f);
        g.fill(shape);
        pop();
    }

    public void drawOutline(String str, Font font, float size, Color color) {
        if(font.getSize() != 100)
            font = font.deriveFont(100f);
        Shape shape = font.layoutGlyphVector(g.getFontRenderContext(), str.toCharArray(), 0, str.length(), 0).getOutline();
        g.setColor(color);
        scale(size / 100f, size / 100f);
        g.draw(shape);
        pop();
    }

    public void drawImage(Image img) {
        g.drawImage(img, 0, 0, null);
    }

    public void draw(HLRenderable renderable) {
        renderable.render(this);
    }

    public void dispose() {
        g.dispose();
    }
}