package fr.hadriel.hgl.g2d;

import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;

/**
 * Created by HaDriel on 10/12/2016.
 */
public class QuadraticBezierCurve implements BatchRenderable {
    private Vec2 start;
    private Vec2 control;
    private Vec2 end;
    private int tesselCount;
    private float strokeWidth;
    private Vec4 color;

    public QuadraticBezierCurve(float sx, float sy, float ex, float ey, float cx, float cy, int tesselCount, float strokeWidth, Vec4 color) {
        this(new Vec2(sx, sy), new Vec2(ex, ey), new Vec2(cx, cy), tesselCount, strokeWidth, color);
    }

    public QuadraticBezierCurve(Vec2 start, Vec2 end, Vec2 control, int tesselCount, float strokeWidth, Vec4 color) {
        this.start = start;
        this.end = end;
        this.control = control;
        this.tesselCount = tesselCount;
        this.strokeWidth = strokeWidth;
        this.color = color;
    }

    private void getPoint(float t, Vec2 v) {
        float x = (1 - t) * (1 - t) * start.x + 2 * (1 - t) * t * control.x + t * t * end.x;
        float y = (1 - t) * (1 - t) * start.y + 2 * (1 - t) * t * control.y + t * t * end.y;
        v.set(x, y);
    }

    public void render(BatchGraphics g) {
        float unit = 1f / tesselCount;
        float t = 0f;
        Vec2 p = new Vec2();
        Vec2 c = new Vec2();
        getPoint(0, p);
        boolean done = false;
        while (!done) {
            if(t > 1f) {
                t = 1f;
                done = true;
            }
            getPoint(t, c);
            g.drawLine(p.x, p.y, c.x, c.y);
            p.set(c);
            t += unit;
        }
    }
}