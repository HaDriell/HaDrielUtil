package fr.hadriel.math.svg;

import fr.hadriel.math.QuadraticBezierCurve;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.geometry.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author glathuiliere
 */
public class Path {

    private static final float DEFAULT_FLATNESS_TOLERANCE = 1f;
    private static final int DEFAULT_SUBDIVISION_CAP = 24;

    private List<SVG.Command> commands;
    private SVG.Command move;
    private SVG.Command last;

    public Path() {
        clear();
    }

    public void clear() {
        this.commands = new ArrayList<>();
        this.move = null;
        this.last = null;
        moveTo(0, 0, false);
    }

    //Shortcuts

    public void moveTo(Vec2 position, boolean relative) {
        moveTo(position.x, position.y, relative);
    }

    public void lineTo(Vec2 position, boolean relative) {
        lineTo(position.x, position.y, relative);
    }

    public void curveTo(Vec2 control, Vec2 position, boolean relative) {
        curveTo(control.x, control.y, position.x, position.y, relative);
    }

    public void curveTo(Vec2 c1, Vec2 c2, Vec2 position, boolean relative) {
        curveTo(c1.x, c1.y, c2.x, c2.y, position.x, position.y, relative);
    }

    public void curveToSmooth(Vec2 position, boolean relative) {
        curveToSmooth(position.x, position.y, relative);
    }

    public void curveToSmooth(Vec2 c2, Vec2 position, boolean relative) {
        curveToSmooth(c2.x, c2.y, position.x, position.y, relative);
    }

    public void arcTo(Vec2 radius, float rotation, boolean large, boolean sweep, Vec2 position, boolean relative) {
        arcTo(radius.x, radius.y, rotation, large, sweep, position.x, position.y, relative);
    }


    // Default implementations

    public void moveTo(float x, float y, boolean relative) {
        move = new SVG.MoveTo(x, y);
        last = move;
        commands.add(last);
    }

    public void lineTo(float x, float y, boolean relative) {
        Vec2 position = last.getPosition();
        if(relative) {
            x += position.x;
            y += position.y;
        }
        last = new SVG.LineTo(position.x, position.y, x, y);
        commands.add(last);
    }

    public void curveTo(float cx, float cy, float x, float y, boolean relative) {
        Vec2 position = last.getPosition();
        if(relative) {
            cx += position.x;
            cy += position.y;

            x += position.x;
            y += position.y;
        }
        last = new SVG.QuadCurveTo(position.x, position.y, cx, cy, x, y);
        commands.add(last);
    }

    public void curveToSmooth(float x, float y, boolean relative) {
        Vec2 position = last.getPosition();
        Vec2 c = last.getSmoothControl();
        if(relative) {
            c = c.add(position);

            x += position.x;
            y += position.y;
        }
        last = new SVG.QuadCurveTo(position.x, position.y, c.x, c.y, x, y);
        commands.add(last);
    }

    public void curveTo(float c1x, float c1y, float c2x, float c2y, float x, float y, boolean relative) {
        Vec2 position = last.getPosition();
        if(relative) {
            c1x += position.x;
            c1y += position.y;

            c2x += position.x;
            c2y += position.y;

            x += position.x;
            y += position.y;
        }
        last = new SVG.CubicCurveTo(position.x, position.y, c1x, c1y, c2x, c2y, x, y);
        commands.add(last);
    }

    public void curveToSmooth(float c2x, float c2y, float x, float y, boolean relative) {
        Vec2 position = last.getPosition();
        Vec2 c1 = last.getSmoothControl();
        if(relative) {
            c1 = c1.add(position);

            c2x += position.x;
            c2y += position.y;

            x += position.x;
            y += position.y;
        }
        last = new SVG.CubicCurveTo(position.x, position.y, c1.x, c1.y, c2x, c2y, x, y);
        commands.add(last);
    }

    public void arcTo(float rx, float ry, float rotation, boolean large, boolean sweep, float x, float y, boolean relative) {

    }


    public void closePath() {
        moveTo(move.getPosition(), false);
    }

    public Polygon toPolygon() {
        return toPolygon(DEFAULT_FLATNESS_TOLERANCE, DEFAULT_SUBDIVISION_CAP);
    }

    public Polygon toPolygon(float flatnessTolerance, int subdivisionCap) {
        List<Vec2> vertexList = segmentize(flatnessTolerance, subdivisionCap);
        Vec2[] vertexArray = new Vec2[vertexList.size()];
        vertexList.toArray(vertexArray);
        return new Polygon(vertexArray);
    }

    public List<Vec2> segmentize(float flatnessTolerance, int subdivisionCap) {
        final List<Vec2> vertices = new ArrayList<>();
        commands.forEach(command -> segmentize(command, vertices, flatnessTolerance, subdivisionCap));
        return vertices;
    }

    private static void segmentize(SVG.Command command, List<Vec2> vertices, float flatnessTolerance, int subdivisionCap) {
        Class type = command.getClass();
        if(type == SVG.LineTo.class) segmentize((SVG.LineTo) command, vertices);
        else if(type == SVG.CubicCurveTo.class) segmentize((SVG.CubicCurveTo) command, vertices, subdivisionCap);
        else if(type == SVG.QuadCurveTo.class) segmentize((SVG.QuadCurveTo) command, vertices, flatnessTolerance, subdivisionCap);
    }

    private static void segmentize(SVG.LineTo command, List<Vec2> vertices) {
        push(vertices, command.a);
        push(vertices, command.b);
    }

    private static void segmentize(SVG.CubicCurveTo command, List<Vec2> vertices, int subdivisionCap) {
        float dt = 1f / subdivisionCap;
        float t = 0;
        while(t < 1f) {
            Vec2 v = command.interpolate(t);
            push(vertices, v);
            t += dt;
        }
    }

    private static void segmentize(SVG.QuadCurveTo command, List<Vec2> vertices, float flatnessTolerance, int subdivisionCap) {
        segmentizeCurve(new QuadraticBezierCurve(command.a, command.control, command.b), vertices, flatnessTolerance, subdivisionCap);
    }

    private static void segmentizeCurve(QuadraticBezierCurve curve, List<Vec2> vertices, float flatnessTolerance2, int subdivisionReserve) {
        if(subdivisionReserve == 0 || curve.getFlatness2() <= flatnessTolerance2) {
            push(vertices, curve.a);
            push(vertices, curve.b);
        } else {
            QuadraticBezierCurve l = new QuadraticBezierCurve();
            QuadraticBezierCurve r = new QuadraticBezierCurve();
            curve.subdivide(l, r);
            segmentizeCurve(l, vertices, flatnessTolerance2, subdivisionReserve - 1);
            segmentizeCurve(r, vertices, flatnessTolerance2, subdivisionReserve - 1);
        }
    }

    private static void push(List<Vec2> list, Vec2 v) {
        if(list.isEmpty() || !list.get(list.size() - 1).equals(v)) {
            list.add(v);
        }
    }
}