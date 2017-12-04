package fr.hadriel.graphics.g2d;

import fr.hadriel.graphics.opengl.TextureRegion;
import fr.hadriel.graphics.renderers.BatchRenderer;
import fr.hadriel.graphics.opengl.Matrix3fStack;
import fr.hadriel.math.*;

/**
 * Created by HaDriel on 11/12/2016.
 */
public class Graphics {

    public static final int BEZIER_SUBDIVISION_COUNT = 25;
    public static final float BEZIER_QUANTUM_T_ADVANCE = 1f / BEZIER_SUBDIVISION_COUNT;

    private float curveFlatnessApproximation = 2f;
    private float strokeWidth = 1f;
    private Matrix3fStack stack;
    private Vec4 color;

    private BatchRenderer batch;

    public Graphics(BatchRenderer batch) {
        this.batch = batch;
        this.stack = new Matrix3fStack();
        this.color = new Vec4();
    }

    public void setCurveFlatnessApproximation(float curveFlatnessApproximation) {
        this.curveFlatnessApproximation = curveFlatnessApproximation;
    }

    public float getCurveFlatnessApproximation() {
        return curveFlatnessApproximation;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setColor(int packed) {
        float r = ((packed >> 24) & 0xFF) / 255f;
        float g = ((packed >> 16) & 0xFF) / 255f;
        float b = ((packed >> 8) & 0xFF) / 255f;
        float a = ((packed >> 0) & 0xFF) / 255f;
        setColor(r, g, b, a);
    }

    public void setColor(Vec4 color) {
        this.color = color == null ? Vec4.ZERO : color;
    }

    public void setColor(float r, float g, float b, float a) {
        color = new Vec4(r, g, b, a);
    }

    public Vec4 getColor() {
        return new Vec4(color);
    }

    public void push(Matrix3f matrix) {
        stack.push(matrix);
    }

    public void pop() {
        stack.pop();
    }

    public void clear() {
        color = new Vec4(1, 1, 1, 1);
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
        Vec2 position;
        Matrix3f matrix = stack.top();
        position = matrix.multiply(x, y);
        batch.submit(position.x, position.y, color, 0, 0, null);
        position = matrix.multiply(dx, y);
        batch.submit(position.x, position.y, color, 0, 0, null);
        position = matrix.multiply(dx, dy);
        batch.submit(position.x, position.y, color, 0, 0, null);
        position = matrix.multiply(x, dy);
        batch.submit(position.x, position.y, color, 0, 0, null);
    }

    public void drawLine(float xa, float ya, float xb, float yb) {
        float weight = strokeWidth / 2;
        Vec2 line = new Vec2(xb - xa, yb - ya);
        Vec2 normal = line.normalLeft().scale(weight, weight);
        Vec2 position;
        Matrix3f matrix = stack.top();

        //Basically draws a quad
        position = matrix.multiply(xa + normal.x, ya + normal.y);
        batch.submit(position.x, position.y, color);
        position = matrix.multiply(xa - normal.x, ya - normal.y);
        batch.submit(position.x, position.y, color);
        position = matrix.multiply(xb - normal.x, yb - normal.y);
        batch.submit(position.x, position.y, color);
        position = matrix.multiply(xb + normal.x, yb + normal.y);
        batch.submit(position.x, position.y, color);
    }

    public void drawBezierCurve(float ax, float ay, float cx, float cy, float bx, float by) {
        drawBezierCurve(new QuadraticBezierCurve(ax, ay, cx, cy, bx, by));
    }

    public void drawBezierCurve(float ax, float ay, float c1x, float c1y, float c2x, float c2y, float bx, float by) {
        drawBezierCurve(new CubicBezierCurve(ax, ay, c1x, c1y, c2x, c2y, bx, by));
    }

    // unoptimized way : draw BEZIER_SUBFIVISION_COUNT segments to represent de cubic bezier curve
    public void drawBezierCurve(CubicBezierCurve curve) {
        float t = 0;
        Vec2 a = curve.getPoint(t);
        Vec2 b;
        for(int i = 0; i < BEZIER_SUBDIVISION_COUNT; i++) {
            //Shift to the next position
            t += BEZIER_QUANTUM_T_ADVANCE;
            if(t > 1f)
                t = 1f;
            b = a;
            a = curve.getPoint(t);
            //Render line
            drawLine(a.x, a.y, b.x, b.y);
        }
   }

    //Recursive divide & conquer method
    public void drawBezierCurve(QuadraticBezierCurve curve) {
        if(curve.getFlatness() < curveFlatnessApproximation) {
            drawLine(curve.a.x, curve.a.y, curve.b.x, curve.b.y);
            return;
        }
        QuadraticBezierCurve l = new QuadraticBezierCurve();
        QuadraticBezierCurve r = new QuadraticBezierCurve();
        curve.subdivide(l, r);
        drawBezierCurve(l);
        drawBezierCurve(r);
    }

    public void drawTextureRegion(float x, float y, float width, float height, TextureRegion region) {
        drawTextureRegion(x, y, width, height, region, null);
    }

    public void drawTextureRegion(float x, float y, float width, float height, TextureRegion region, Vec4 color) {
        Vec2 position;
        Matrix3f matrix = stack.top();
        position = matrix.multiply(x, y);
        batch.submit(position.x, position.y, color, region.u[0], region.v[0], region.texture);
        position = matrix.multiply(x + width, y);
        batch.submit(position.x, position.y, color, region.u[1], region.v[1], region.texture);
        position = matrix.multiply(x + width, y + height);
        batch.submit(position.x, position.y, color, region.u[2], region.v[2], region.texture);
        position = matrix.multiply(x, y + height);
        batch.submit(position.x, position.y, color, region.u[3], region.v[3], region.texture);
    }

    public void drawTextureRegion(float x, float y, TextureRegion region, Vec4 color) {
        drawTextureRegion(x, y, region.width, region.height, region, color);
    }

    public void drawTextureRegion(float x, float y, TextureRegion region) {
        drawTextureRegion(x, y, region.width, region.height, region);
    }
}