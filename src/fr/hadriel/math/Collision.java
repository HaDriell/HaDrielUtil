package fr.hadriel.math;

import fr.hadriel.math.geom.Polygon;

/**
 * Created by glathuiliere on 06/09/2016.
 */
public final class Collision {

    public static boolean pointToAABB(float x, float y, float bx, float by, float bwidth, float bheight) {
        return Mathf.contains(x, bx, bx + bwidth) && Mathf.contains(y, by, by + bheight);
    }

    public static Vec2 lineToLine(Vec2 a0, Vec2 b0, Vec2 a1, Vec2 b1) {
        return lineToLine(a0.x, a0.y, b0.x, b0.y, a1.x, a1.y, b1.x, b1.y);
    }

    public static Vec2 lineToLine(float p0x, float p0y, float p1x, float p1y, float p2x, float p2y, float p3x, float p3y) {
        float A1 = p1y - p0y;
        float B1 = p0x - p1x;
        float C1 = A1 * p0x + B1 * p0y;

        float A2 = p3y - p2y;
        float B2 = p2x - p3x;
        float C2 = A2 * p2x + B2 * p2y;

        float denominator = A1 * B2 - A2 * B1;

        if(denominator == 0) {
            return null;
        }

        float x = (B2 * C1 - B1 * C2) / denominator;
        float y = (A1 * C2 - A2 * C1) / denominator;
        return new Vec2(x, y);
    }

    public static Vec2 segmentToSegment(Vec2 a0, Vec2 b0, Vec2 a1, Vec2 b1) {
        return segmentToSegment(a0.x, a0.y, b0.x, b0.y, a1.x, a1.y, b1.x, b1.y);
    }

    public static Vec2 segmentToSegment(float p0x, float p0y, float p1x, float p1y, float p2x, float p2y, float p3x, float p3y) {
        float A1 = p1y - p0y;
        float B1 = p0x - p1x;
        float C1 = A1 * p0x + B1 * p0y;

        float A2 = p3y - p2y;
        float B2 = p2x - p3x;
        float C2 = A2 * p2x + B2 * p2y;

        float denominator = A1 * B2 - A2 * B1;

        if(denominator == 0) {
            return null;
        }

        float x = (B2 * C1 - B1 * C2) / denominator;
        float y = (A1 * C2 - A2 * C1) / denominator;
        float rx0 = (x - p0x) / (p1x - p0x);
        float ry0 = (y - p0y) / (p1y - p0y);
        float rx1 = (x - p2x) / (p3x - p2x);
        float ry1 = (y - p2y) / (p3y - p2y);

        if( (Mathf.contains(rx0, 0, 1) || Mathf.contains(ry0, 0, 1)) &&
                (Mathf.contains(rx1, 0, 1) || Mathf.contains(ry1, 0, 1)) )
            return new Vec2(x, y);
        return null;
    }

    //SAT implementation
    public static boolean polygonToPolygon(Polygon a, Polygon b) {
        Vec2[] normals;
        Interval ia, ib;

        normals = a.getNormals();
        for(Vec2 axis : normals) {
            ia = a.getProjection(axis);
            ib = b.getProjection(axis);
            if(Interval.getOverlapValue(ia, ib) == 0) return false;
        }

        normals = b.getNormals();
        for(Vec2 axis : normals) {
            ia = a.getProjection(axis);
            ib = b.getProjection(axis);
            if(Interval.getOverlapValue(ia, ib) == 0) return false;
        }

        return true;
    }
}