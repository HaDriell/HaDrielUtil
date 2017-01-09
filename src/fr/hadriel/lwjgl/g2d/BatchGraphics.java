package fr.hadriel.lwjgl.g2d;

import fr.hadriel.lwjgl.opengl.TextureRegion;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;

/**
 * Created by HaDriel setOn 11/12/2016.
 */
public class BatchGraphics {

    private float strokeWidth = 1f;
    private Matrix4fStack stack;
    private Vec4 color;

    private BatchRenderer batch;

    public BatchGraphics(BatchRenderer batch) {
        this.batch = batch;
        this.stack = new Matrix4fStack();
        this.color = new Vec4();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setColor(float r, float g, float b, float a) {
        color.set(r, g, b, a);
    }

    public Vec4 getColor() {
        return new Vec4(color);
    }

    public void push(Matrix4f matrix) {
        stack.push(matrix);
    }

    public void pop() {
        stack.pop();
    }

    public void clear() {
        color.set(1, 1, 1, 1);
        strokeWidth = 1f;
        stack.clear();
    }

    public void begin() {
        clear();
        batch.begin();
    }

    public void end() {
        batch.end();
    }

    public void drawRect(float x, float y, float width, float height) {
        float dx = x + width;
        float dy = y + height;
        drawLine(x, y, dx, y);
        drawLine(dx, y, dx, dy);
        drawLine(dx, dy, x, dy);
        drawLine(x, dy, x, y);
    }

    public void fillRect(float x, float y, float width, float height) {
        float dx = x + width;
        float dy = y + height;
        Vec2 p = new Vec2();
        Matrix4f matrix = stack.top();
        matrix.multiply(p.set(x, y));
        batch.submit(p.x, p.y, color, 0, 0, null);
        matrix.multiply(p.set(dx, y));
        batch.submit(p.x, p.y, color, 0, 0, null);
        matrix.multiply(p.set(dx, dy));
        batch.submit(p.x, p.y, color, 0, 0, null);
        matrix.multiply(p.set(x, dy));
        batch.submit(p.x, p.y, color, 0, 0, null);
    }

    public void drawLine(float xa, float ya, float xb, float yb) {
        float weight = strokeWidth / 2;
        Vec2 line = new Vec2(xb - xa, yb - ya);
        Vec2 heavyness = line.getNormalLeft().scale(weight, weight);
        Vec2 p = new Vec2();
        Matrix4f matrix = stack.top();
        matrix.multiply(p.set(xa + heavyness.x, ya + heavyness.y));
        batch.submit(p.x, p.y, color);
        matrix.multiply(p.set(xa - heavyness.x, ya - heavyness.y));
        batch.submit(p.x, p.y, color);
        matrix.multiply(p.set(xb - heavyness.x, yb - heavyness.y));
        batch.submit(p.x, p.y, color);
        matrix.multiply(p.set(xb + heavyness.x, yb + heavyness.y));
        batch.submit(p.x, p.y, color);
    }

    public void drawTextureRegion(float x, float y, float width, float height, TextureRegion region) {
        drawTextureRegion(x, y, width, height, region, null);
    }

    public void drawBezierCurve(float sx, float sy, float cx, float cy, float ex, float ey) {
        float dx1 = cx - sx;
        float dy1 = cy - sy;
        float dx2 = ex - cx;
        float dy2 = ey - cy;
        Vec2 c = new Vec2(); //current
        Vec2 p = new Vec2(); //previous
        float stepTime = 1f / Mathf.min(Mathf.sqrt(dx1 * dx1 + dy1 * dy1) + Mathf.sqrt(dx2 * dx2 + dy2 * dy2), 25); // stepTime calculation

        //Bézier Curve approximation
        c.set(sx, sy);
        float t = 0;
        while (t < 1f) {
            p.set(c);
            t += stepTime;
            if(t > 1f) t = 1f;
            c.set(
                    (1 - t) * (1 - t) * sx + 2 * (1 - t) * t * cx + t * t * ex,
                    (1 - t) * (1 - t) * sy + 2 * (1 - t) * t * cy + t * t * ey
            );
            drawLine(p.x, p.y, c.x, c.y);
        }
    }

    public void drawTextureRegion(float x, float y, float width, float height, TextureRegion region, Vec4 color) {
        Vec2 p = new Vec2();
        Matrix4f matrix = stack.top();
        matrix.multiply(p.set(x, y));
        batch.submit(p.x, p.y, color, region.u[0], region.v[0], region.texture);
        matrix.multiply(p.set(x + width, y));
        batch.submit(p.x, p.y, color, region.u[1], region.v[1], region.texture);
        matrix.multiply(p.set(x + width, y + height));
        batch.submit(p.x, p.y, color, region.u[2], region.v[2], region.texture);
        matrix.multiply(p.set(x, y + height));
        batch.submit(p.x, p.y, color, region.u[3], region.v[3], region.texture);
    }
}