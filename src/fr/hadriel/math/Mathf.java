package fr.hadriel.math;

/**
 * Created by glathuiliere on 13/07/2016.
 */
public class Mathf {
    public static final float PI = 3.14159265358979323846264338327950288419716939937510582f;

    public static float min(float a, float b) {
        if (a < b)
            return a;
        return b;
    }

    public static float max(float a, float b) {
        if (a > b)
            return a;
        return b;
    }

    public static float clamp(float min, float max, float value) {
        if (value < min)
            value = min;
        if (value > max)
            value = max;

        return value;
    }

    public static float cos(float radians) {
        return (float) Math.cos(radians);
    }

    public static float sin(float radians) {
        return (float) Math.sin(radians);
    }

    public static float tan(float radians) {
        return (float) Math.tan(radians);
    }

    public static float acos(float radians) {
        return (float) Math.acos(radians);
    }

    public static float asin(float radians) {
        return (float) Math.asin(radians);
    }

    public static float atan(float radians) {
        return (float) Math.atan(radians);
    }

    public static float atan2(float x, float y) {
        return (float) Math.atan2(y, x);
    }

    public static float toRadians(float degrees) {
        return (float) Math.toRadians(degrees);
    }

    public static float toDegrees(float radians) {
        return (float) Math.toDegrees(radians);
    }

    public static float sqrt(float radians) {
        return (float) Math.sqrt(radians);
    }

    public static float floor(float radians) {
        return (float) Math.floor(radians);
    }

    public static float ceil(float radians) {
        return (float) Math.ceil(radians);
    }

    public static float round(float radians) {
        return (float) Math.round(radians);
    }

    public static float random() {
        return (float) Math.random();
    }

    public static float random(float a, float b) {
        return (float) a + random() * (b - a);
    }

    public static float abs(float a) {
        return (float) Math.abs(a);
    }

    public static float pow(float f, float p) {
        return (float) Math.pow(f, p);
    }

    public static float lerp(float start, float end, float t) {
        return start + (end - start) * t;
    }

    public static Vec2 lerp(Vec2 start, Vec2 end, float t) {
        return new Vec2(lerp(start.x, end.x, t), lerp(start.y, end.y, t));
    }
}