package fr.hadriel.gui;

import fr.hadriel.graphics.opengl.TextureRegion;
import fr.hadriel.graphics.renderers.BatchRenderer2D;
import fr.hadriel.graphics.opengl.Matrix3fStack;
import fr.hadriel.math.*;

/**
 * Created by HaDriel on 11/12/2016.
 */
public class Graphics {

    //TODO : setup beter settings options
    public static final class Settings {
        public float strokeWidth;
        public float bzFlatnessFactor;
        public int bzCubicSubdivisionCount;

        public Settings() {
            reset();
        }

        public void reset() {
            this.strokeWidth = 1f;
            this.bzFlatnessFactor = 1f;
            this.bzCubicSubdivisionCount = 25;
        }
    }

    private Vec4 color;
    private final Settings settings;
    private final Matrix3fStack stack;

    private BatchRenderer2D batch;

    public Graphics(float width, float height) {
        this(0, width, 0, height);
    }

    public Graphics(float left, float right, float top, float bottom) {
        this(new BatchRenderer2D(left, right, top, bottom));
    }

    public Graphics(BatchRenderer2D batch) {
        this.settings = new Settings();
        this.batch = batch;
        this.stack = new Matrix3fStack();
        this.color = new Vec4();
    }

    public Settings settings() {
        return settings;
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
        settings.reset();
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
        batch.vertex(position.x, position.y, color, 0, 0, null);
        position = matrix.multiply(dx, y);
        batch.vertex(position.x, position.y, color, 0, 0, null);
        position = matrix.multiply(dx, dy);
        batch.vertex(position.x, position.y, color, 0, 0, null);
        position = matrix.multiply(x, dy);
        batch.vertex(position.x, position.y, color, 0, 0, null);
        batch.indices(0, 1, 2, 2, 3, 0);
    }

    public void drawLine(float xa, float ya, float xb, float yb) {
        float weight = settings.strokeWidth / 2;
        Vec2 lineVector = new Vec2(xb - xa, yb - ya);
        Vec2 normal = lineVector.left().normalize().scale(weight, weight);
        Vec2 position;
        Matrix3f matrix = stack.top();

        //Basically draws a quad
        position = matrix.multiply(xa + normal.x, ya + normal.y);
        batch.vertex(position.x, position.y, color);
        position = matrix.multiply(xa - normal.x, ya - normal.y);
        batch.vertex(position.x, position.y, color);
        position = matrix.multiply(xb - normal.x, yb - normal.y);
        batch.vertex(position.x, position.y, color);
        position = matrix.multiply(xb + normal.x, yb + normal.y);
        batch.vertex(position.x, position.y, color);
        batch.indices(0, 1, 2, 2, 3, 0);
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
        float dt = 1f / settings.bzCubicSubdivisionCount;
        Vec2 a = curve.getPoint(t);
        Vec2 b;
        for(int i = 0; i < settings.bzCubicSubdivisionCount; i++) {
            //Shift to the next position
            t += dt;
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
        if(curve.getFlatness() < settings.bzFlatnessFactor) {
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
        batch.vertex(position.x, position.y, color, region.uvs[0].x, region.uvs[0].y, region.texture);
        position = matrix.multiply(x + width, y);
        batch.vertex(position.x, position.y, color, region.uvs[1].x, region.uvs[1].y, region.texture);
        position = matrix.multiply(x + width, y + height);
        batch.vertex(position.x, position.y, color, region.uvs[2].x, region.uvs[2].y, region.texture);
        position = matrix.multiply(x, y + height);
        batch.vertex(position.x, position.y, color, region.uvs[3].x, region.uvs[3].y, region.texture);
        batch.indices(0, 1, 2, 2, 3, 0);
    }

    public void drawTextureRegion(float x, float y, TextureRegion region, Vec4 color) {
        drawTextureRegion(x, y, region.width, region.height, region, color);
    }

    public void drawTextureRegion(float x, float y, TextureRegion region) {
        drawTextureRegion(x, y, region.width, region.height, region);
    }
}