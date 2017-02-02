package fr.hadriel.math;

/**
 * Created by glathuiliere on 13/06/2016.
 */
public class Vec2 {

    public static final Vec2 X = new Vec2(1, 0);
    public static final Vec2 Y = new Vec2(0, 1);
    public static final Vec2 ZERO = new Vec2();

    public float x;
    public float y;

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
        return x ==0 && y == 0;
    }

    public Vec2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vec2 scale(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vec2 add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vec2 sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vec2 clamp(float min, float max) {
        if(x < min) x = min;
        if(y < min) y = min;
        if(x > max) x = max;
        if(y > max) y = max;
        return this;
    }

    public Vec2 set(Vec2 v) {
        return set(v.x, v.y);
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
        return (float) Math.sqrt(x * x + y * y);
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
        x = nx;
        y = ny;
        return this;
    }

    public Vec2 rotate(float angle, Vec2 origin) {
        sub(origin);
        rotate(angle);
        add(origin);
        return this;
    }

    public Vec2 normalize() {
        float invLen = 1f / len();
        if(invLen != 0)
            scale(invLen, invLen);
        return this;
    }

    public Vec2 getNormalLeft() {
        return new Vec2(y, -x).normalize();
    }

    public Vec2 getNormalRight() {
        return new Vec2(-y, x).normalize();
    }

    public Vec2 transform(Matrix3f matrix) {
        set(matrix.getTransformed(this));
        return this;
    }

    public Vec2 transform(Matrix4f matrix) {
        return matrix.multiply(this);
    }

    public Vec2 getTransformed(Matrix3f matrix) {
        return matrix.getTransformed(this);
    }

    public Vec2 copy() {
        return new Vec2(this.x, this.y);
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