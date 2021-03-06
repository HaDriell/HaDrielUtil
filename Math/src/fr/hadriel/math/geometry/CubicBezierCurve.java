package fr.hadriel.math.geometry;

import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author glathuiliere
 */
public class CubicBezierCurve {

    public Vec2 a;
    public Vec2 c1;
    public Vec2 c2;
    public Vec2 b;

    public CubicBezierCurve() {}

    public CubicBezierCurve(CubicBezierCurve copy) {
        this.a = copy.a;
        this.c1 = copy.c1;
        this.c2 = copy.c2;
        this.b = copy.b;
    }

    public CubicBezierCurve(Vec2 a, Vec2 c1, Vec2 c2, Vec2 b) {
        this.a = a;
        this.c1 = c1;
        this.c2 = c2;
        this.b = b;
    }

    public CubicBezierCurve(float ax, float ay, float c1x, float c1y, float c2x, float c2y, float bx, float by) {
        this(new Vec2(ax, ay), new Vec2(c1x, c1y), new Vec2(c2x, c2y), new Vec2(bx, by));
    }

    public void setCurve(float ax, float ay, float c1x, float c1y, float c2x, float c2y, float bx, float by) {
        setCurve(new Vec2(ax, ay), new Vec2(c1x, c1y), new Vec2(c2x, c2y), new Vec2(bx, by));
    }

    public float getPolylineLength2() {
        return a.distance2(c1) + c1.distance2(c2) + c2.distance2(b);
    }

    public float getPolylineLength() {
        return Mathf.sqrt(getPolylineLength2());
    }

    public Vec2 getPoint(float t) {
        return Mathf.bezier(t, a, c1, c2, b);
    }

    public float getFlatness2() {
        return Mathf.max(
                Mathf.distanceToLine2(a.x, a.y, b.x, b.y, c1.x, c1.y),
                Mathf.distanceToLine2(a.x, a.y, b.x, b.y, c2.x, c2.y)
        );
    }

    public float getFlatness() {
        return Mathf.max(
                Mathf.distanceToLine(a.x, a.y, b.x, b.y, c1.x, c1.y),
                Mathf.distanceToLine(a.x, a.y, b.x, b.y, c2.x, c2.y)
        );
    }

    public void setCurve(Vec2 a, Vec2 c1, Vec2 c2, Vec2 b) {
        this.a = a;
        this.c1 = c1;
        this.c2 = c2;
        this.b = b;
    }

    public void subdivide(CubicBezierCurve left, CubicBezierCurve right) {
        Vec2 center = c1.add(c2).scale(0.5F);
        Vec2 ctrl1 = a.add(c1).scale(0.5F);
        Vec2 ctrl4 = b.add(c2).scale(0.5F);
        Vec2 ctrl2 = center.add(ctrl1).scale(0.5F);
        Vec2 ctrl3 = center.add(ctrl4).scale(0.5F);
        if(left != null)
            left.setCurve(a, ctrl1, ctrl2, center);
        if(right != null)
            right.setCurve(center, ctrl3, ctrl4, b);
    }

    public void subdivisions(List<CubicBezierCurve> curves, float flatnessTolerance2) {
        if(curves == null)
            return;

        if(getFlatness2() <= flatnessTolerance2)
            curves.add(new CubicBezierCurve(this)); // deep copy
        else {
            CubicBezierCurve l = new CubicBezierCurve();
            CubicBezierCurve r = new CubicBezierCurve();
            subdivide(l, r);
            l.subdivisions(curves, flatnessTolerance2); // semi recursive
            r.subdivisions(curves, flatnessTolerance2); // semi recursive
        }
    }

    public List<Vec2> toVertices(int subdivisionCount) {
        List<Vec2> vertices = new ArrayList<>(subdivisionCount + 1);
        float t = 0f;
        float dt = 1f / subdivisionCount;
        for(int i = 0; i < subdivisionCount; i++) {
            vertices.add(Mathf.bezier(t, a, c1, c2, b));
            t += dt;
        }
        vertices.add(Mathf.bezier(1f, a, c1, c2, b));
        return vertices;
    }
}