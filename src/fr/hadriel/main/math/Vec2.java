package fr.hadriel.main.math;

/**
 * Created by glathuiliere on 13/06/2016.
 */
public class Vec2 {

    public static final Vec2 X = new Vec2(1, 0);
    public static final Vec2 Y = new Vec2(0, 1);
    public static final Vec2 ZERO = new Vec2();

    public final float x;
    public final float y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2() {
        this(0, 0);
    }

    public Vec2(Vec2 v) {
        this(v.x, v.y);
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public Vec2 add(float x, float y) {
        float dx = this.x + x;
        float dy = this.y + y;
        return new Vec2(dx, dy);
    }

    public Vec2 sub(float x, float y) {
        float dx = this.x - x;
        float dy = this.y - y;
        return new Vec2(dx, dy);
    }

    public Vec2 scale(float x, float y) {
        float dx = this.x * x;
        float dy = this.y * y;
        return new Vec2(dx, dy);
    }

    public Vec2 clamp(float min, float max) {
        float cx = x;
        float cy = y;
        if(x < min) cx = min;
        if(y < min) cy = min;
        if(x > max) cx = max;
        if(y > max) cy = max;
        return new Vec2(cx, cy);
    }

    public Vec2 scale(Vec2 v) {
        return scale(v.x, v.y);
    }

    public Vec2 add(Vec2 v) {
        return add(v.x, v.y);
    }

    public Vec2 sub(Vec2 v) {
        return sub(v.x, v.y);
    }

    public float len2() {
        return x * x + y * y;
    }

    public float len() {
        return Mathf.sqrt(x * x + y * y);
    }

    public float dot(Vec2 v) {
        return x * v.x + y * v.y;
    }

    public float cross(Vec2 v) {
        return x * v.y - y * v.x;
    }

    public float distance(Vec2 v) {
        float dx = x - v.x;
        float dy = y - v.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float distance2(Vec2 v) {
        float dx = x - v.x;
        float dy = y - v.y;
        return dx * dx + dy * dy;
    }

    public float angle(Vec2 v) {
        return (float) Math.atan2(cross(v), dot(v));
    }

    //Rotates counter clockwise
    public Vec2 rotate(float angle) {
        double rad = Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        float nx = x * cos - y * sin;
        float ny = x * sin + y * cos;
        return new Vec2(nx, ny);
    }

    public Vec2 rotate(float angle, Vec2 origin) {
        return sub(origin).rotate(angle).add(origin); //May be optimized to avoid all these useless instanciations
    }

    public Vec2 normalize() {
        float length = len();
        if(length == 0) return ZERO;
        float ilength = 1f / length;
        return scale(ilength, ilength);
    }

    public Vec2 invert() {
        return new Vec2(-x, -y);
    }

    public Vec2 normalLeft() {
        float length = len();
        if(length == 0) return ZERO;
        return new Vec2(y / length, -x / length);
    }

    public Vec2 normalRight() {
        float length = len();
        if(length == 0) return ZERO;
        return new Vec2(-y / length, x / length);
    }

    public Vec2 transform(Matrix3f matrix) {
        return matrix.multiply(this);
    }

    public Vec2 transform(Matrix4f matrix) {
        return matrix.multiply(this);
    }

    public String toString() {
        return String.format("(%.2f; %.2f)", x, y);
    }

    public static float orientation(Vec2 a, Vec2 b, Vec2 c) {
        float acx = a.x - c.x;
        float bcx = b.x - c.x;
        float acy = a.y - c.y;
        float bcy = b.y - c.y;
        return acx * bcy - acy * bcx;
    }
}