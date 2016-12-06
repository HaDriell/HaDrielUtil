package fr.hadriel.math;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public class Vec4 {

    public static final Vec4 X = new Vec4(1, 0, 0, 0);
    public static final Vec4 Y = new Vec4(0, 1, 0, 0);
    public static final Vec4 Z = new Vec4(0, 0, 1, 0);
    public static final Vec4 W = new Vec4(0, 0, 0, 1);


    public float x;
    public float y;
    public float z;
    public float w;

    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
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

    public Vec4 set(Vec4 v) { return set(v.x, v.y, v.z, v.w); }
    public Vec4 add(Vec4 v) { return add(v.x, v.y, v.z, v.w); }
    public Vec4 sub(Vec4 v) { return sub(v.x, v.y, v.z, v.w); }
    public Vec4 mul(Vec4 v) { return mul(v.x, v.y, v.z, v.w); }
    public Vec4 div(Vec4 v) { return div(v.x, v.y, v.z, v.w); }

    public Vec4 set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Vec4 add(float x, float y, float z, float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }

    public Vec4 sub(float x, float y, float z, float w) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        this.w -= w;
        return this;
    }

    public Vec4 mul(float x, float y, float z, float w) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        this.w *= w;
        return this;
    }

    public Vec4 div(float x, float y, float z, float w) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        this.w /= w;
        return this;
    }


    public Vec4 reverse() {
        x = -x;
        y = -y;
        z = -z;
        w = -w;
        return this;
    }

    public float magnitude() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    public Vec4 normalize() {
        float mag = magnitude();
        return new Vec4(x / mag, y / mag, z / mag, w / mag);
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