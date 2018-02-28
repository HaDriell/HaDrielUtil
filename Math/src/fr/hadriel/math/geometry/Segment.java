package fr.hadriel.math.geometry;

import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;

public class Segment {

    public final Vec2 a;
    public final Vec2 b;
    public final float length;

    public Segment(Vec2 a, Vec2 b) {
        this.a = a;
        this.b = b;
        this.length = a.distance(b);
    }

    public Vec2 left() {
        float dx = (b.x - a.x);
        float dy = (b.y - a.y);
        float len = Mathf.sqrt(dx*dx + dy*dy);
        if(len == 0) return Vec2.ZERO;
        return new Vec2(dy / len, -dx / len);
    }

    public Vec2 right() {
        float dx = (b.x - a.x);
        float dy = (b.y - a.y);
        float len = Mathf.sqrt(dx*dx + dy*dy);
        if(len == 0) return Vec2.ZERO;
        return new Vec2(-dy / len, dx / len);
    }

    public static Vec2 getNearestPointOnLine(Vec2 p, Vec2 a, Vec2 b) {
        Vec2 ap = p.sub(a);
        Vec2 line = b.sub(a);
        float ab2 = line.len2();
        //length is 0
        if(ab2 <= Epsilon.E) return a;
        float ap_ab = ap.dot(line);
        float t = ap_ab / ab2;
        return line.scale(t, t).add(a);
    }

    public static Vec2 getNearestPointOnSegment(Vec2 p, Vec2 a, Vec2 b) {
        Vec2 ap = p.sub(a);
        Vec2 line = b.sub(a);
        float ab2 = line.len2();
        //length is 0
        if(ab2 <= Epsilon.E) return a;
        float ap_ab = ap.dot(line);
        float t = ap_ab / ab2;
        t = Mathf.clamp(0f, 1f, t);
        return line.scale(t, t).add(a);
    }
}