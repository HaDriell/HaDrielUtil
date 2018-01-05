package fr.hadriel.math.svg;

import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;

/**
 *
 * @author glathuiliere
 */
public final class SVG {

    public static Path parse(String source) {
        Path path = new Path();
        SVGParser parser = new SVGParser(source);
        parser.allowcomma = false;
        while (!parser.isDone()) {
            parser.allowcomma = false;
            char cmd = parser.getChar();
            switch (cmd) {
                case 'M':
                    path.moveTo(parser.vec2(), false);
                    while (parser.nextIsNumber()) {
                        path.lineTo(parser.vec2(), false);
                    }
                    break;
                case 'm':
                    path.moveTo(parser.vec2(), true);
                    while (parser.nextIsNumber()) {
                        path.lineTo(parser.vec2(), true);
                    }
                    break;
                case 'L':
                    path.lineTo(parser.vec2(), false);
                    while (parser.nextIsNumber())
                        path.lineTo(parser.vec2(), false);
                    break;
                case 'l':
                    path.lineTo(parser.vec2(), true);
                    while (parser.nextIsNumber())
                        path.lineTo(parser.vec2(), true);
                    break;
                case 'H':
                    path.lineTo(parser.vec2H(), false);
                    while (parser.nextIsNumber())
                        path.lineTo(parser.vec2H(), false);
                    break;
                case 'h':
                    path.lineTo(parser.vec2H(), true);
                    while (parser.nextIsNumber())
                        path.lineTo(parser.vec2H(), true);
                    break;
                case 'V':
                    path.lineTo(parser.vec2V(), false);
                    while (parser.nextIsNumber())
                        path.lineTo(parser.vec2V(), false);
                    break;
                case 'v':
                    path.lineTo(parser.vec2V(), true);
                    while (parser.nextIsNumber())
                        path.lineTo(parser.vec2V(), true);
                    break;
                case 'Q':
                    path.curveTo(parser.vec2(), parser.vec2(), false);
                    while (parser.nextIsNumber())
                        path.curveToSmooth(parser.vec2(), parser.vec2(), false);
                    break;
                case 'q':
                    path.curveTo(parser.vec2(), parser.vec2(), true);
                    while (parser.nextIsNumber())
                        path.curveTo(parser.vec2(), parser.vec2(), true);
                    break;
                case 'T':
                    path.curveToSmooth(parser.vec2(), false);
                    while (parser.nextIsNumber())
                        path.curveTo(parser.vec2(), parser.vec2(), false);
                    break;
                case 't':
                    path.curveToSmooth(parser.vec2(), true);
                    while (parser.nextIsNumber())
                        path.curveTo(parser.vec2(), parser.vec2(), true);
                    break;
                case 'C':
                    path.curveTo(parser.vec2(), parser.vec2(), parser.vec2(), false);
                    while (parser.nextIsNumber())
                        path.curveTo(parser.vec2(), parser.vec2(), parser.vec2(), false);
                    break;
                case 'c':
                    path.curveTo(parser.vec2(), parser.vec2(), parser.vec2(), true);
                    while (parser.nextIsNumber())
                        path.curveTo(parser.vec2(), parser.vec2(), parser.vec2(), true);
                    break;
                case 'S':
                    path.curveToSmooth(parser.vec2(), parser.vec2(), false);
                    while (parser.nextIsNumber())
                        path.curveToSmooth(parser.vec2(), parser.vec2(), false);
                    break;
                case 's':
                    path.curveToSmooth(parser.vec2(), parser.vec2(), true);
                    while (parser.nextIsNumber())
                        path.curveToSmooth(parser.vec2(), parser.vec2(), true);
                    break;
                case 'A':
                    path.arcTo(parser.vec2(), parser.a(), parser.b(), parser.b(), parser.vec2(), false);
                    while (parser.nextIsNumber())
                        path.arcTo(parser.vec2(), parser.a(), parser.b(), parser.b(), parser.vec2(), false);
                    break;
                case 'a':
                    path.arcTo(parser.vec2(), parser.a(), parser.b(), parser.b(), parser.vec2(), true);
                    while (parser.nextIsNumber())
                        path.arcTo(parser.vec2(), parser.a(), parser.b(), parser.b(), parser.vec2(), true);
                    break;
                case 'Z': case 'z': path.closePath(); break;
                default:
                    throw new IllegalArgumentException("invalid command ("+cmd+ ") in SVG path at pos="+parser.pos);
            }
            parser.allowcomma = false;
        }
        return path;
    }


    //SVG Primitives Implentations
    public static interface Command {
        public Vec2 interpolate(float t);
        public Vec2 getSmoothControl();
        public Vec2 getPosition();
    }

    public static final class LineTo implements Command {
        public final Vec2 a;
        public final Vec2 b;

        public LineTo(float ax, float ay, float bx, float by) {
            a = new Vec2(ax, ay);
            b = new Vec2(bx, by);
        }

        public Vec2 interpolate(float t) {
            return Mathf.lerp(t, a, b);
        }

        public Vec2 getSmoothControl() {
            return b;
        }

        public Vec2 getPosition() {
            return b;
        }
    }

    public static final class MoveTo implements Command {
        public final Vec2 position;

        public MoveTo(float x, float y) {
            this.position = new Vec2(x, y);
        }

        public Vec2 interpolate(float t) {
            throw new UnsupportedOperationException("Shouldn't call interpolations on MoveTo SVG Commands");
        }

        public Vec2 getSmoothControl() {
            return position;
        }

        public Vec2 getPosition() {
            return position;
        }
    }

    public static final class QuadCurveTo implements Command {

        public final Vec2 a;
        public final Vec2 control;
        public final Vec2 b;

        public QuadCurveTo(float ax, float ay, float cx, float cy, float bx, float by) {
            this.a = new Vec2(ax, ay);
            this.control = new Vec2(cx, cy);
            this.b = new Vec2(bx, by);
        }

        public float getFlatness2() {
            return Mathf.distanceToLine2(a.x, a.y, b.x, b.y, control.x, control.y);
        }

        public Vec2 interpolate(float t) {
            return Mathf.bezier(t, a, control, b);
        }

        public Vec2 getSmoothControl() {
            return new Vec2(b.x * 2f - a.x, control.y * 2f - control.y);
        }

        public Vec2 getPosition() {
            return b;
        }
    }

    public static final class CubicCurveTo implements Command {

        public final Vec2 a;
        public final Vec2 c1;
        public final Vec2 c2;
        public final Vec2 b;

        public CubicCurveTo(float ax, float ay, float c1x, float c1y, float c2x, float c2y, float bx, float by) {
            this.a = new Vec2(ax, ay);
            this.c1 = new Vec2(c1x, c1y);
            this.c2 = new Vec2(c2x, c2y);
            this.b = new Vec2(bx, by);
        }

        public Vec2 interpolate(float t) {
            return Mathf.bezier(t, a, c1, c2, b);
        }

        public Vec2 getSmoothControl() {
            return new Vec2(b.x * 2f - a.x, c2.y * 2f - c2.y);
        }

        public Vec2 getPosition() {
            return b;
        }
    }
}