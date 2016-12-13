package fr.hadriel.graphics;

import fr.hadriel.math.Matrix3f;

import java.awt.*;

/**
 * Created by glathuiliere setOn 08/08/2016.
 */
public class HLGraphics {

    private Graphics2D g;
    private MatrixStack matrixStack;
    private ClipStack clipStack;

    public HLGraphics(Graphics2D g) {
        this.g = g;
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        this.matrixStack = new MatrixStack();
        this.clipStack = new ClipStack(g.getClip());
    }

    public void pushClip(Shape shape) {
        clipStack.push(shape);
        g.setClip(clipStack.top());
    }

    public void popClip() {
        clipStack.pop();
        g.setClip(clipStack.top());
    }

    public void pushTransform(Matrix3f matrix) {
        matrixStack.push(matrix);
        g.setTransform(matrixStack.top().toAffineTransform());
    }

    public void popTransform() {
        matrixStack.pop();
        g.setTransform(matrixStack.top().toAffineTransform());
    }

    public void translate(float x, float y) {
        matrixStack.top().translate(x, y);
        g.setTransform(matrixStack.top().toAffineTransform());
    }

    public void rotate(float angle) {
        matrixStack.top().rotate(angle);
        g.setTransform(matrixStack.top().toAffineTransform());
    }

    public void scale(float x, float y) {
        matrixStack.top().scale(x, y);
        g.setTransform(matrixStack.top().toAffineTransform());
    }

    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }

    public void drawOval(int x, int y, int width, int height, Color color) {
        g.setColor(color);
        g.drawOval(x, y, width, height);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int count, Color color) {
        g.setColor(color);
        g.drawPolygon(xPoints, yPoints, count);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int count, Color color) {
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, count);
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
        Shape shape = font.createGlyphVector(g.getFontRenderContext(), str).getOutline();
        g.setColor(color);
        pushTransform(Matrix3f.Scale(size / 100f, size / 100f));
        g.fill(shape);
        popTransform();
    }

    public void drawOutline(String str, Font font, float size, Color color) {
        if(font.getSize() != 100)
            font = font.deriveFont(100f);
        Shape shape = font.createGlyphVector(g.getFontRenderContext(), str).getOutline();
        g.setColor(color);
        pushTransform(Matrix3f.Scale(size / 100f, size / 100f));
        g.draw(shape);
        popTransform();
    }

    public void drawImage(Image img) {
        g.drawImage(img, 0, 0, null);
    }

    public void drawImageStretched(Image img, int width, int height) {
        g.drawImage(img, 0, 0, width, height, null);
    }

    public void drawImageRegion(Image img, int x, int y, int width, int height) {
        g.drawImage(img, 0, 0, width, height, x, y, width, height, null);
    }

    public void draw(HLRenderable renderable) {
        renderable.render(this);
    }

    public void draw(Shape shape) {
        g.draw(shape);
    }

    public void dispose() {
        g.dispose();
    }
}