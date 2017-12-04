package fr.hadriel.math;

/**
 * Created by glathuiliere on 13/07/2016.
 */
public class Mathf {
    public static final float PI = 3.14159265358979323846264338327950288419716939937510582354687f;

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

    public static boolean contains(float value, float min, float max) {
        return value >= min && value <= max;
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

    public static float bezier(float t, float a, float b, float c) {
        float it = 1 - t;
        return (it * it * a) + (2 * it * t * b) + (t * t * c);
    }

    public static float bezier(float t, float a, float b, float c, float d) {
        float it = 1 - t;
        return (it * it * it * a) + (3 * it * it * t * b) + (3 * it * t * t * c) + (t * t * t * d);
    }

    public static Vec2 bezier(float t, Vec2 a, Vec2 b, Vec2 c) {
        float x = bezier(t, a.x, b.x, c.x);
        float y = bezier(t, a.y, b.y, c.y);
        return new Vec2(x, y);
    }

    public static Vec2 bezier(float t, Vec2 a, Vec2 b, Vec2 c, Vec2 d) {
        float x = bezier(t, a.x, b.x, c.x, d.x);
        float y = bezier(t, a.y, b.y, c.y, d.y);
        return new Vec2(x, y);
    }

    public static float distanceToLine(float x1, float y1, float x2, float y2, float px, float py) {
        return sqrt(distanceToLine2(x1, y1, x2, y2, px, py));
    }

    public static float distanceToLine2(float x1, float y1, float x2, float y2, float px, float py) {
        // Adjust vectors relative to x1,y1
        // x2,y2 becomes relative vector from x1,y1 to b of segment
        x2 -= x1;
        y2 -= y1;
        // px,py becomes relative vector from x1,y1 to test point
        px -= x1;
        py -= y1;
        float dotprod = px * x2 + py * y2;
        float projlenSq;
        if (dotprod <= 0f) {
            // px,py is on the side of x1,y1 away from x2,y2
            // distance to segment is length of px,py vector
            // "length of its (clipped) projection" is now 0.0
            projlenSq = 0f;
        } else {
            // switch to backwards vectors relative to x2,y2
            // x2,y2 are already the negative of x1,y1=>x2,y2
            // to get px,py to be the negative of px,py=>x2,y2
            // the dot product of two negated vectors is the same
            // as the dot product of the two normal vectors
            px = x2 - px;
            py = y2 - py;
            dotprod = px * x2 + py * y2;
            if (dotprod <= 0f) {
                // px,py is on the side of x2,y2 away from x1,y1
                // distance to segment is length of (backwards) px,py vector
                // "length of its (clipped) projection" is now 0.0
                projlenSq = 0f;
            } else {
                // px,py is between x1,y1 and x2,y2
                // dotprod is the length of the px,py vector
                // projected on the x2,y2=>x1,y1 vector times the
                // length of the x2,y2=>x1,y1 vector
                projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
            }
        }
        // Distance to line is now the length of the relative point
        // vector minus the length of its projection onto the line
        // (which is zero if the projection falls outside the range
        //  of the line segment).
        float lenSq = px * px + py * py - projlenSq;
        if (lenSq < 0f) {
            lenSq = 0f;
        }
        return lenSq;
    }
}