package fr.hadriel.renderers;

import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.asset.graphics.image.ImageRegion;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec4;

import java.util.Stack;

public class BatchGraphics {
    private static final Vec4 WHITE = new Vec4(1, 1, 1, 1);

    private final BatchRenderer renderer;
    private final Stack<Matrix3> matrixStack;

    public BatchGraphics() {
        this.renderer = new BatchRenderer();
        this.matrixStack = new Stack<>();
    }

    public void setProjection(float left, float right, float top, float bottom) {
        renderer.setProjection(left, right, top, bottom);
    }

    public void setSDFSettings(float buffer, float gamma) {
        renderer.setFontBuffer(buffer);
        renderer.setFontGamma(gamma);
    }

    public void begin() {
        renderer.begin();
        matrixStack.clear();
        matrixStack.push(Matrix3.Identity);
    }

    public void push(Matrix3 matrix) {
        matrixStack.push( matrixStack.peek().multiply(matrix) );
    }

    public void pop() {
        if (matrixStack.size() > 1) matrixStack.pop(); // protect stack from being empty
    }

    public void draw(float x, float y, ImageRegion sprite) {
        draw(x, y, sprite.texture.width, sprite.texture.height, sprite, WHITE);
    }

    public void draw(float x, float y, ImageRegion sprite, Vec4 color) {
        draw(x, y, sprite.texture.width, sprite.texture.height, sprite, color);
    }

    public void draw(float x, float y, float width, float height, ImageRegion sprite) {
        draw(x, y, width, height, sprite, WHITE);
    }

    public void draw(float x, float y, float width, float height, ImageRegion sprite, Vec4 color) {
        if (sprite == null) return;
        renderer.draw(matrixStack.peek(), x, y, width, height, sprite, color);
    }

    public void draw(float x, float y, String text, Font font, float fontSize) {
        draw(x, y, text, font, fontSize, WHITE);
    }

    public void draw(float x, float y, String text, Font font, float fontSize, Vec4 color) {
        if (text == null || text.isEmpty()) return;
        renderer.draw(matrixStack.peek(), x, y, text, font, fontSize, color);
    }

    public void end() {
        renderer.end();
    }
}