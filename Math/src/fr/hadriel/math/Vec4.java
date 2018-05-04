package fr.hadriel.math;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public class Vec4 {

    public static final Vec4 X = new Vec4(1, 0, 0, 0);
    public static final Vec4 Y = new Vec4(0, 1, 0, 0);
    public static final Vec4 Z = new Vec4(0, 0, 1, 0);
    public static final Vec4 W = new Vec4(0, 0, 0, 1);
    public static final Vec4 ZERO = new Vec4(0, 0, 0, 0);

    public final float x;
    public final float y;
    public final float z;
    public final float w;

    public Vec4(double x, double y, double z, double w) { this((float) x, (float) y, (float) z, (float) w); }
    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4(Vec3 v, float w) {
        this(v.x, v.y, v.z, w);
    }

    public Vec4(Vec2 v, float z, float w) {
        this(v.x, v.y, z, w);
    }

    public Vec4() {
        this(0, 0, 0, 0);
    }

    public Vec4(Vec4 v) {
        this(v.x, v.y, v.z, v.w);
    }

    public Vec4 copy() {
        return new Vec4(this);
    }

    public Vec4 add(Vec4 v) { return add(v.x, v.y, v.z, v.w); }
    public Vec4 sub(Vec4 v) { return sub(v.x, v.y, v.z, v.w); }
    public Vec4 mul(Vec4 v) { return mul(v.x, v.y, v.z, v.w); }

    public Vec4 add(float x, float y, float z, float w) {
        float dx = this.x + x;
        float dy = this.y + y;
        float dz = this.z + z;
        float dw = this.w + w;
        return new Vec4(dx, dy, dz, dw);
    }

    public Vec4 sub(float x, float y, float z, float w) {
        float dx = this.x - x;
        float dy = this.y - y;
        float dz = this.z - z;
        float dw = this.w - w;
        return new Vec4(dx, dy, dz, dw);
    }

    public Vec4 mul(float x, float y, float z, float w) {
        float dx = this.x * x;
        float dy = this.y * y;
        float dz = this.z * z;
        float dw = this.w * w;
        return new Vec4(dx, dy, dz, dw);
    }

    public Vec4 invert() {
        return new Vec4(-x, -y, -z, -w);
    }

    public float len() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    public Vec4 normalize() {
        float mag = len();
        return mag == 0 ? ZERO : new Vec4(x / mag, y / mag, z / mag, w / mag);
    }

    public float dot(Vec4 v) {
        return x * v.x + v.y * y + z * v.z + w * v.w;
    }

    public float distance(Vec4 v) {
        float dx = x - v.x;
        float dy = y - v.y;
        float dz = z - v.z;
        float dw = w - v.w;
        return (float) Math.sqrt(dx*dx + dy*dy + dz*dz + dw*dw);
    }

    public String toString() {
        return String.format("(%.2f, %.2f, %.2f, %.2f)", x, y, z, w);
    }
}