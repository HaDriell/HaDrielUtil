package fr.hadriel.graphics;

import fr.hadriel.graphics.opengl.TextureRegion;
import fr.hadriel.graphics.renderers.BatchRenderer2D;
import fr.hadriel.graphics.opengl.Matrix3fStack;
import fr.hadriel.math.*;
import fr.hadriel.math.geometry.Polygon;

/**
 * Created by HaDriel on 11/12/2016.
 */
public class Graphics implements IGraphics {

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
    }

    public Settings settings() {
        return settings;
    }

    public void push(Matrix3f matrix) {
        stack.push(matrix);
    }

    public void pop() {
        stack.pop();
    }

    public void clear() {
        settings.reset();
        stack.clear();
    }

    public void begin() {
        clear();
        batch.begin();
    }

    public void draw(IDrawable drawable) {
        drawable.draw(this);
    }

    public void draw(float x, float y, Polygon polygon) {
        for(int i = 0; i < polygon.vertices.length; i++) {
            Vec2 a = polygon.vertices[i];
            Vec2 b = i + 1 == polygon.vertices.length ? polygon.vertices[0] : polygon.vertices[i + 1];
            drawLine(a.x, a.y, b.x, b.y);
        }
    }

    public void fill(float x, float y, Polygon polygon) {
        Vec2 position;
        Matrix3f matrix = stack.top();
        int[] indices = new int[polygon.vertices.length];
        for(int i = 0; i < polygon.vertices.length; i++) {
            position = matrix.multiply(polygon.vertices[i].x + x, polygon.vertices[i].y + y);
            batch.vertex(position.x, position.y, settings.color(), 0, 0, null);
            indices[i] = i;
        }
        batch.indices(indices);
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
        batch.vertex(position.x, position.y, settings.color(), 0, 0, null);
        position = matrix.multiply(dx, y);
        batch.vertex(position.x, position.y, settings.color(), 0, 0, null);
        position = matrix.multiply(dx, dy);
        batch.vertex(position.x, position.y, settings.color(), 0, 0, null);
        position = matrix.multiply(x, dy);
        batch.vertex(position.x, position.y, settings.color(), 0, 0, null);
        batch.indices(0, 3, 2, 2, 1, 0);
    }

    public void drawLine(float xa, float ya, float xb, float yb) {
        float weight = settings.strokeWidth / 2;
        Vec2 lineVector = new Vec2(xb - xa, yb - ya);
        Vec2 normal = lineVector.left().normalize().scale(weight, weight);
        Vec2 position;
        Matrix3f matrix = stack.top();

        //Basically draws a quad
        position = matrix.multiply(xa + normal.x, ya + normal.y);
        batch.vertex(position.x, position.y, settings.color());
        position = matrix.multiply(xa - normal.x, ya - normal.y);
        batch.vertex(position.x, position.y, settings.color());
        position = matrix.multiply(xb - normal.x, yb - normal.y);
        batch.vertex(position.x, position.y, settings.color());
        position = matrix.multiply(xb + normal.x, yb + normal.y);
        batch.vertex(position.x, position.y, settings.color());
        batch.indices(0, 1, 2, 2, 3, 0);
    }

    public void drawCurve(float ax, float ay, float cx, float cy, float bx, float by) {
        drawCurve(new QuadraticBezierCurve(ax, ay, cx, cy, bx, by));
    }

    public void drawCurve(float ax, float ay, float c1x, float c1y, float c2x, float c2y, float bx, float by) {
        drawCurve(new CubicBezierCurve(ax, ay, c1x, c1y, c2x, c2y, bx, by));
    }

    // unoptimized way : draw BEZIER_SUBFIVISION_COUNT segments to represent de cubic bezier curve
    public void drawCurve(CubicBezierCurve curve) {
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
    public void drawCurve(QuadraticBezierCurve curve) {
        if(curve.getFlatness() < settings.bzFlatnessFactor) {
            drawLine(curve.a.x, curve.a.y, curve.b.x, curve.b.y);
            return;
        }
        QuadraticBezierCurve l = new QuadraticBezierCurve();
        QuadraticBezierCurve r = new QuadraticBezierCurve();
        curve.subdivide(l, r);
        drawCurve(l);
        drawCurve(r);
    }

    public void drawTexture(float x, float y, float width, float height, TextureRegion region) {
        drawTexture(x, y, width, height, region, null);
    }

    public void drawTexture(float x, float y, float width, float height, TextureRegion region, Vec4 mask) {
        Vec2 position;
        Matrix3f matrix = stack.top();
        position = matrix.multiply(x, y);
        batch.vertex(position.x, position.y, mask, region.uvs[0].x, region.uvs[0].y, region.texture);
        position = matrix.multiply(x + width, y);
        batch.vertex(position.x, position.y, mask, region.uvs[1].x, region.uvs[1].y, region.texture);
        position = matrix.multiply(x + width, y + height);
        batch.vertex(position.x, position.y, mask, region.uvs[2].x, region.uvs[2].y, region.texture);
        position = matrix.multiply(x, y + height);
        batch.vertex(position.x, position.y, mask, region.uvs[3].x, region.uvs[3].y, region.texture);
        batch.indices(0, 3, 2, 2, 1, 0);
    }

    public void drawTexture(float x, float y, TextureRegion region, Vec4 mask) {
        drawTexture(x, y, region.width, region.height, region, mask);
    }

    public void drawTexture(float x, float y, TextureRegion region) {
        drawTexture(x, y, region.width, region.height, region);
    }
}