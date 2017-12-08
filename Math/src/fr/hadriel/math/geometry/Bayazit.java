/*
 * Copyright (c) 2010-2016 William Bittle  http://www.dyn4j.org/
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions 
 *     and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 *     and the following disclaimer in the documentation and/or other materials provided with the 
 *     distribution.
 *   * Neither the name of dyn4j nor the names of its contributors may be used to endorse or 
 *     promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fr.hadriel.math.geometry;

import fr.hadriel.math.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the Bayazit convex decomposition algorithm for simple polygons.
 * <p>
 * This algorithm is a O(nr) complexity algorithm where n is the number of input vertices and r is the number of
 * output convex polygons.  This algorithm can achieve optimal decompositions, however this is not guaranteed.
 * @author William Bittle
 * @version 3.1.10
 * @since 2.2.0
 * @see <a href="http://mnbayazit.com/406/bayazit" target="_blank">Bayazit</a>
 */
public class Bayazit {

    /* (non-Javadoc)
     * @see org.dyn4j.geometry.decompose.Decomposer#decompose(org.dyn4j.geometry.Vec2[])
     */
    public static List<Convex> decompose(Vec2... points) {
        // check for null array
        if (points == null) throw new NullPointerException("geometry.decompose.nullArray");
        // get the number of points
        int size = points.length;
        // check the size
        if (size < 4) throw new IllegalArgumentException("geometry.decompose.invalidSize");

        // get the winding order
        float winding = Geometry.getWinding(points);

        // reverse the array if the points are in clockwise order
        if (winding < 0.0) {
            Geometry.reverseWinding(points);
        }

        // create a list for the points to go in
        List<Vec2> polygon = new ArrayList<>();

        // copy the points to the list
        Collections.addAll(polygon, points);

        // create a list for the polygons to live
        List<Convex> polygons = new ArrayList<>();

        // decompose the polygon
        decomposePolygon(polygon, polygons);

        // return the result
        return polygons;
    }

    /**
     * Internal recursive method to decompose the given polygon into convex sub-polygons.
     * @param polygon the polygon to decompose
     * @param polygons the list to store the convex polygons resulting from the decomposition
     */
    public static void decomposePolygon(List<Vec2> polygon, List<Convex> polygons) {
        // get the size of the given polygon
        int size = polygon.size();

        // initialize
        Vec2 upperIntersection = new Vec2();
        Vec2 lowerIntersection = new Vec2();
        double upperDistance = Double.MAX_VALUE;
        double lowerDistance = Double.MAX_VALUE;
        double closestDistance = Double.MAX_VALUE;
        int upperIndex = 0;
        int lowerIndex = 0;
        int closestIndex = 0;

        List<Vec2> lower = new ArrayList<>();
        List<Vec2> upper = new ArrayList<>();

        // loop over all the vertices
        for (int i = 0; i < size; i++) {
            // get the current vertex
            Vec2 p = polygon.get(i);

            // get the adjacent vertices
            Vec2 p0 = polygon.get(i - 1 < 0 ? size - 1 : i - 1);
            Vec2 p1 = polygon.get(i + 1 == size ? 0 : i + 1);

            // check if the vertex is a reflex vertex
            if (isReflex(p0, p, p1)) {

                // loop over the vertices to determine if both extended
                // adjacent edges intersect one edge (in which case a
                // steiner point will be added)
                for (int j = 0; j < size; j++) {
                    Vec2 q = polygon.get(j);

                    // get the adjacent vertices
                    Vec2 q0 = polygon.get(j - 1 < 0 ? size - 1 : j - 1);
                    Vec2 q1 = polygon.get(j + 1 == size ? 0 : j + 1);

                    // create a storage location for the intersection point
                    Vec2 s = new Vec2();

                    // extend the previous edge
                    // does the line p0->p go between the vertices q and q0
                    if (left(p0, p, q) && rightOn(p0, p, q0)) {
                        // get the intersection point
                        if (getIntersection(p0, p, q, q0, s)) {
                            // make sure the intersection point is to the right of
                            // the edge p1->p (this makes sure its inside the polygon)
                            if (right(p1, p, s)) {
                                // get the distance from p to the intersection point s
                                double dist = p.distance2(s);
                                // only save the smallest
                                if (dist < lowerDistance) {
                                    lowerDistance = dist;
                                    lowerIntersection = s;
                                    lowerIndex = j;
                                }
                            }
                        }
                    }

                    // extend the next edge
                    // does the line p1->p go between q and q1
                    if (left(p1, p, q1) && rightOn(p1, p, q)) {
                        // get the intersection point
                        if (getIntersection(p1, p, q, q1, s)) {
                            // make sure the intersection point is to the left of
                            // the edge p0->p (this makes sure its inside the polygon)
                            if (left(p0, p, s)) {
                                // get the distance from p to the intersection point s
                                double dist = p.distance2(s);
                                // only save the smallest
                                if (dist < upperDistance) {
                                    upperDistance = dist;
                                    upperIntersection = s;
                                    upperIndex = j;
                                }
                            }
                        }
                    }
                }

                // if the lower index and upper index are equal then this means
                // that the range of p only included an edge (both extended previous
                // and next edges of p only intersected the same edge, therefore no
                // point exists within that range to connect to)
                if (lowerIndex == (upperIndex + 1) % size) {
                    // create a steiner point in the middle
                    Vec2 s = upperIntersection.add(lowerIntersection).scale(0.5f);

                    // partition the polygon
                    if (i < upperIndex) {
                        lower.addAll(polygon.subList(i, upperIndex + 1));
                        lower.add(s);
                        upper.add(s);
                        if (lowerIndex != 0) upper.addAll(polygon.subList(lowerIndex, size));
                        upper.addAll(polygon.subList(0, i + 1));
                    } else {
                        if (i != 0) lower.addAll(polygon.subList(i, size));
                        lower.addAll(polygon.subList(0, upperIndex + 1));
                        lower.add(s);
                        upper.add(s);
                        upper.addAll(polygon.subList(lowerIndex, i + 1));
                    }
                } else {
                    // otherwise we need to find the closest "visible" point to p

                    if (lowerIndex > upperIndex) {
                        upperIndex += size;
                    }

                    closestIndex = lowerIndex;
                    // find the closest visible point
                    for (int j = lowerIndex; j <= upperIndex; j++) {
                        int jmod = j % size;
                        Vec2 q = polygon.get(jmod);

                        if (q == p || q == p0 || q == p1) continue;

                        // check the distance first, since this is generally
                        // a much faster operation than checking if its visible
                        double dist = p.distance2(q);
                        if (dist < closestDistance) {
                            if (isVisible(polygon, i, jmod)) {
                                closestDistance = dist;
                                closestIndex = jmod;
                            }
                        }
                    }

                    // once we find the closest partition the polygon
                    if (i < closestIndex) {
                        lower.addAll(polygon.subList(i, closestIndex + 1));
                        if (closestIndex != 0) upper.addAll(polygon.subList(closestIndex, size));
                        upper.addAll(polygon.subList(0, i + 1));
                    } else {
                        if (i != 0) lower.addAll(polygon.subList(i, size));
                        lower.addAll(polygon.subList(0, closestIndex + 1));
                        upper.addAll(polygon.subList(closestIndex, i + 1));
                    }
                }

                // decompose the smaller first
                if (lower.size() < upper.size()) {
                    decomposePolygon(lower, polygons);
                    decomposePolygon(upper, polygons);
                } else {
                    decomposePolygon(upper, polygons);
                    decomposePolygon(lower, polygons);
                }

                // if the given polygon contains a reflex vertex, then return
                return;
            }
        }

        // if we get here, we know the given polygon has 0 reflex vertices
        // and is therefore convex, add it to the list of convex polygons
        if (polygon.size() < 3) {
            throw new IllegalArgumentException("geometry.decompose.crossingEdges");
        }
        Vec2[] vertices = new Vec2[polygon.size()];
        polygon.toArray(vertices);
        polygons.add(new Convex(vertices));
    }

    /**
     * Returns true if the given vertex, b, is a reflex vertex.
     * <p>
     * A reflex vertex is a vertex who's interior angle is greater
     * than 180 degrees.
     * @param p0 the vertex to test
     * @param p the previous vertex
     * @param p1 the next vertex
     * @return boolean
     */
    public static boolean isReflex(Vec2 p0, Vec2 p, Vec2 p1) {
        // if the point p is to the right of the line p0-p1 then
        // the point is a reflex vertex
        return right(p1, p0, p);
    }

    /**
     * Returns true if the given point p is to the left
     * of the line created by a-b.
     * @param a the first point of the line
     * @param b the second point of the line
     * @param p the point to test
     * @return boolean
     */
    public static boolean left(Vec2 a, Vec2 b, Vec2 p) {
        return getLocation(p, a, b) > 0;
    }

    /**
     * Returns true if the given point p is to the left
     * or on the line created by a-b.
     * @param a the first point of the line
     * @param b the second point of the line
     * @param p the point to test
     * @return boolean
     */
    public static boolean leftOn(Vec2 a, Vec2 b, Vec2 p) {
        return getLocation(p, a, b) >= 0;
    }

    /**
     * Returns true if the given point p is to the right
     * of the line created by a-b.
     * @param a the first point of the line
     * @param b the second point of the line
     * @param p the point to test
     * @return boolean
     */
    public static boolean right(Vec2 a, Vec2 b, Vec2 p) {
        return getLocation(p, a, b) < 0;
    }

    /**
     * Returns true if the given point p is to the right
     * or on the line created by a-b.
     * @param a the first point of the line
     * @param b the second point of the line
     * @param p the point to test
     * @return boolean
     */
    public static boolean rightOn(Vec2 a, Vec2 b, Vec2 p) {
        return getLocation(p, a, b) <= 0;
    }

    /**
     * Returns true if the given lines intersect and returns the intersection point in
     * the p parameter.
     * @param a1 the first point of the first line
     * @param a2 the second point of the first line
     * @param b1 the first point of the second line
     * @param b2 the second point of the second line
     * @param p the destination object for the intersection point
     * @return boolean
     */
    public static boolean getIntersection(Vec2 a1, Vec2 a2, Vec2 b1, Vec2 b2, Vec2 p) {
        // any point on a line can be found by the parametric equation:
        // P = (1 - t)A + tB
        // or
        // P.x = (1 - t)A.x + tB.x
        // P.y = (1 - t)A.y + tB.y

        // so we get:

        // P.x = (1 - t1)A1.x + t1A2.x
        // P.y = (1 - t1)A1.y + t1A2.y

        // P.x = (1 - t2)B1.x + t2B2.x
        // P.y = (1 - t2)B1.y + t2B2.y


        // since P is the same we can set the equations equal

        // (1 - t1)A1.x + t1A2.x = (1 - t2)B1.x + t2B2.x
        // (1 - t1)A1.y + t1A2.y = (1 - t2)B1.y + t2B2.y

        // A1.x - t1A1.x + t1A2.x = B1.x - t2B1.x + t2B2.x
        // A1.y - t1A1.y + t1A2.y = B1.y - t2B1.y + t2B2.y

        // t2(B1.x - B2.x) - t1(A1.x - A2.x) = B1.x - A1.x
        // t2(B1.y - B2.y) - t1(A1.y - A2.y) = B1.y - A1.y

        // solve the system of equations

        // t1 = -(B1.x - A1.x - t2(B1.x - B2.x)) / (A1.x - A2.x)
        // t2(B2.y - B2.y) + (B1.x - A1.x - t2(B1.x - B2.x)) / (A1.x - A2.x) * (A1.y - A2.y) = B1.y - A1.y
        // t2(B2.y - B2.y)(A1.x - A2.x) + (B1.x - A1.x - t2(B1.x - B2.x))(A1.y - A2.y) = (B1.y - A1.y)(A1.x - A2.x)
        // t2S2.yS1.x + B1.xS1.y - A1.xS1.y - t2S2.xS1.y = B1.yS1.x - A1.yS1.x
        // t2(S2.yS1.x - S2.xS1.y) = B1.yS1.x - A1.yS1.x - B1.xS1.y + A1.xS1.y
        // t2(S1.cross(S2)) = B1.yS1.x - B1.xS1.y + A1.xS1.y - A1.yS1.x
        // t2(S1.cross(S2)) = A1.cross(S1) - B1.cross(S1)
        // t2 = (A1.cross(S1) - B1.cross(S1)) / S1.cross(S2)

        // if S1.cross(S2) is near zero then there is no solution

        // compute S1 and S2
        Vec2 s1 = a1.sub(a2);
        Vec2 s2 = b1.sub(b2);

        // compute the cross product (the determinant if we used matrix solving techniques)
        float det = s1.cross(s2);

        // make sure the matrix isn't singular (the lines could be parallel)
        if (Math.abs(det) <= Epsilon.E) {
            // return false since there is no way that the segments could be intersecting
            return false;
        } else {
            // pre-divide the determinant
            det = 1f / det;

            // compute t2
            float t2 = det * (a1.cross(s1) - b1.cross(s1));

            // compute the intersection point
            // P = B1(1.0 - t2) + B2(t2)
            p = new Vec2(b1.x * (1f - t2) + b2.x * t2, b1.y * (1f - t2) + b2.y * t2);

            // return that they intersect
            return true;
        }
    }

    /**
     * Returns true if the vertex at index i can see the vertex at index j.
     * @param polygon the current polygon
     * @param i the ith vertex
     * @param j the jth vertex
     * @return boolean
     * @since 3.1.10
     */
    public static boolean isVisible(List<Vec2> polygon, int i, int j) {
        int s = polygon.size();
        Vec2 iv0, iv, iv1;
        Vec2 jv0, jv, jv1;

        iv0 = polygon.get(i == 0 ? s - 1 : i - 1);
        iv = polygon.get(i);
        iv1 = polygon.get(i + 1 == s ? 0 : i + 1);

        jv0 = polygon.get(j == 0 ? s - 1 : j - 1);
        jv = polygon.get(j);
        jv1 = polygon.get(j + 1 == s ? 0 : j + 1);

        // can i see j
        if (isReflex(iv0, iv, iv1)) {
            if (leftOn(iv, iv0, jv) && rightOn(iv, iv1, jv)) return false;
        } else {
            if (rightOn(iv, iv1, jv) || leftOn(iv, iv0, jv)) return false;
        }
        // can j see i
        if (isReflex(jv0, jv, jv1)) {
            if (leftOn(jv, jv0, iv) && rightOn(jv, jv1, iv)) return false;
        } else {
            if (rightOn(jv, jv1, iv) || leftOn(jv, jv0, iv)) return false;
        }
        // make sure the segment from i to j doesn't intersect any edges
        for (int k = 0; k < s; k++) {
            int ki1 = k + 1 == s ? 0 : k + 1;
            if (k == i || k == j || ki1 == i || ki1 == j) continue;
            Vec2 k1 = polygon.get(k);
            Vec2 k2 = polygon.get(ki1);

            Vec2 in = getSegmentIntersection(iv, jv, k1, k2);
            if (in != null) return false;
        }

        return true;
    }

    public static double getLocation(Vec2 point, Vec2 a, Vec2 b) {
        return (b.x - a.x) * (point.y - a.y) -
                (point.x - a.x) * (b.y - a.y);
    }

    public static Vec2 getSegmentIntersection(Vec2 a, Vec2 b, Vec2 c, Vec2 d) {
        return null;
    }

    public static Vec2 getLineIntersection(Vec2 ap1, Vec2 ap2, Vec2 bp1, Vec2 bp2) {
        Vec2 A = ap1.to(ap2);
        Vec2 B = bp1.to(bp2);

        // compute the bottom
        float BxA = B.cross(A);
        if (Math.abs(BxA) <= Epsilon.E) {
            // the lines are parallel and don't intersect
            return null;
        }

        // compute the top
        float ambxA = ap1.sub(bp1).cross(A);
        if (Math.abs(ambxA) <= Epsilon.E) {
            // the lines are coincident
            return null;
        }



        // compute tb
        float tb = ambxA / BxA;
        if (tb < 0.0 || tb > 1.0) {
            // no intersection
            return null;
        }

        // compute the intersection point
        Vec2 ip = B.scale(tb).add(bp1);

        // since both are segments we need to verify that
        // ta is also valid.
        // compute ta
        float ta = ip.sub(ap1).dot(A) / A.dot(A);
        if (ta < 0.0 || ta > 1.0) {
            // no intersection
            return null;
        }

        return ip;
    }
}