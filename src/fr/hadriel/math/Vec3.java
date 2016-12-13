package fr.hadriel.math;

/**
 * Created by glathuiliere setOn 28/11/2016.
 */
public class Vec3 {

    public static final Vec3 X = new Vec3(1, 0, 0);
    public static final Vec3 Y = new Vec3(0, 1, 0);
    public static final Vec3 Z = new Vec3(0, 0, 1);

    public float x;
    public float y;
    public float z;

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(Vec2 v, float z) {
        this(v.x, v.y, z);
    }

    public Vec3() {
        this(0, 0, 0);
    }

    public Vec3(Vec3 v) {
        this(v.x, v.y, v.z);
    }

    public Vec3 copy() {
        return new Vec3(this);
    }

    public Vec3 set(Vec3 v) { return set(v.x, v.y, v.z); }
    public Vec3 add(Vec3 v) { return add(v.x, v.y, v.z); }
    public Vec3 sub(Vec3 v) { return sub(v.x, v.y, v.z); }
    public Vec3 mul(Vec3 v) { return mul(v.x, v.y, v.z); }
    public Vec3 div(Vec3 v) { return div(v.x, v.y, v.z); }

    public Vec3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vec3 add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vec3 sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vec3 mul(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vec3 div(float x, float y, float z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }


    public Vec3 reverse() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public float magnitude() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    public Vec3 normalize() {
        float mag = magnitude();
        return new Vec3(x / mag, y / mag, z / mag);
    }

    public Vec3 cross(Vec3 v) {
        return new Vec3(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
    }

    public float dot(Vec3 v) {
        return x * v.x + v.y * y + z * v.z;
    }

    public float distance(Vec3 v) {
        float dx = x - v.x;
        float dy = y - v.y;
        float dz = z - v.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}