package fr.hadriel.math;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class QuadraticBezierCurve {
    public Vec2 a;
    public Vec2 control;
    public Vec2 b;

    public QuadraticBezierCurve() {
        this.a = Vec2.ZERO;
        this.control = Vec2.ZERO;
        this.b = Vec2.ZERO;
    }

    public QuadraticBezierCurve(Vec2 a, Vec2 control, Vec2 b) {
        this.a = a;
        this.control = control;
        this.b = b;
    }

    public QuadraticBezierCurve(float ax, float ay, float cx, float cy, float bx, float by) {
        this(new Vec2(ax, ay), new Vec2(cx, cy), new Vec2(bx, by));
    }

    public void setCurve(float ax, float ay, float cx, float cy, float bx, float by) {
        setCurve(new Vec2(ax, ay), new Vec2(cx, cy), new Vec2(bx, by));
    }

    public void setCurve(Vec2 a, Vec2 control, Vec2 b) {
        this.a = a;
        this.control = control;
        this.b = b;
    }

    public Vec2 getPoint(float t) {
        return Mathf.bezier(t, a, control, b);
    }

    public float getFlatness2() {
        return Mathf.distanceToLine2(a.x, a.y, b.x, b.y, control.x, control.y);
    }

    public float getFlatness() {
        return Mathf.distanceToLine(a.x, a.y, b.x, b.y, control.x, control.y);
    }

    public void subdivide(QuadraticBezierCurve left, QuadraticBezierCurve right) {
        Vec2 control1 = a.add(control).scale(0.5F);
        Vec2 control2 = b.add(control).scale(0.5F);
        Vec2 mid = control1.add(control2).scale(0.5F);
        if(left != null)
            left.setCurve(a, control1, mid);
        if(right != null)
            right.setCurve(mid, control2, b);
    }
}
