package fr.hadriel.graphics.svg;

import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;

import java.util.ArrayList;
import java.util.List;

import static fr.hadriel.graphics.svg.SVGCommand.*;
/**
 *
 * @author glathuiliere
 */
public class SVGPath {

    private List<Vec2> vertices;
    private Vec2 current; // last point fed into vertices
    private Vec2 previousControl; // last control point (for smooth curves)
    private Vec2 previousMove; // last Move position (for close)

    private List<SVGCommand> commands;

    public SVGPath() {
        this.vertices = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.current = Vec2.ZERO;
        this.previousControl = Vec2.ZERO;
        this.previousMove = Vec2.ZERO;
    }

    public SVGPath(String svg) {
        SVGParser parser = new SVGParser(svg);
        parser.allowcomma = false;
        while (!parser.isDone()) {
            parser.allowcomma = false;
            char cmd = parser.getChar();
            switch (cmd) {
            case 'M':
                moveTo(parser.vec2(), false);
                while (parser.nextIsNumber()) {
                    lineTo(parser.vec2(), false);
                }
                break;
            case 'm':
                moveTo(parser.vec2(), true);
                while (parser.nextIsNumber()) {
                    lineTo(parser.vec2(), true);
                }
                break;
            case 'L':
                lineTo(parser.vec2(), false);
                while (parser.nextIsNumber())
                    lineTo(parser.vec2(), false);
                break;
            case 'l':
                lineTo(parser.vec2(), true);
                while (parser.nextIsNumber())
                    lineTo(parser.vec2(), true);
                break;
            case 'H':
                lineTo(parser.vec2H(), false);
                while (parser.nextIsNumber())
                    lineTo(parser.vec2H(), false);
                break;
            case 'h':
                lineTo(parser.vec2H(), true);
                while (parser.nextIsNumber())
                    lineTo(parser.vec2H(), true);
                break;
            case 'V':
                lineTo(parser.vec2V(), false);
                while (parser.nextIsNumber())
                    lineTo(parser.vec2V(), false);
                break;
            case 'v':
                lineTo(parser.vec2V(), true);
                while (parser.nextIsNumber())
                    lineTo(parser.vec2V(), true);
                break;
            case 'Q':
                curveTo(parser.vec2(), parser.vec2(), false);
                while (parser.nextIsNumber())
                    curveToSmooth(parser.vec2(), parser.vec2(), false);
                break;
            case 'q':
                curveTo(parser.vec2(), parser.vec2(), true);
                while (parser.nextIsNumber())
                    curveTo(parser.vec2(), parser.vec2(), true);
                break;
            case 'T':
                curveToSmooth(parser.vec2(), false);
                while (parser.nextIsNumber())
                    curveTo(parser.vec2(), parser.vec2(), false);
                break;
            case 't':
                curveToSmooth(parser.vec2(), true);
                while (parser.nextIsNumber())
                    curveTo(parser.vec2(), parser.vec2(), true);
                break;
            case 'C':
                curveTo(parser.vec2(), parser.vec2(), parser.vec2(), false);
                while (parser.nextIsNumber())
                    curveTo(parser.vec2(), parser.vec2(), parser.vec2(), false);
                break;
            case 'c':
                curveTo(parser.vec2(), parser.vec2(), parser.vec2(), true);
                while (parser.nextIsNumber())
                    curveTo(parser.vec2(), parser.vec2(), parser.vec2(), true);
                break;
            case 'S':
                curveToSmooth(parser.vec2(), parser.vec2(), false);
                while (parser.nextIsNumber())
                    curveToSmooth(parser.vec2(), parser.vec2(), false);
                break;
            case 's':
                curveToSmooth(parser.vec2(), parser.vec2(), true);
                while (parser.nextIsNumber())
                    curveToSmooth(parser.vec2(), parser.vec2(), true);
                break;
            case 'A':
                throw new UnsupportedOperationException("Arcs are not supported yet");
            case 'a':
                throw new UnsupportedOperationException("Arcs are not supported yet");
            case 'Z': case 'z': closePath(); break;
            default:
                throw new IllegalArgumentException("invalid command ("+cmd+ ") in SVG path at pos="+parser.pos);
            }
        parser.allowcomma = false;
        }
    }

    /**
     * moves the cursor to the position
     * @param position the position
     * @param relative defines if position is relative to the cursor
     */
    public void moveTo(Vec2 position, boolean relative) {
        commands.add(MOVE_TO);
        vertices.add(current = relative ? current.add(position) : position);
        previousMove = current;
        previousControl = current;
    }

    /**
     * draws a line to the position
     * @param position the position
     * @param relative defines if position is relative to the cursor
     */
    public void lineTo(Vec2 position, boolean relative) {
        commands.add(LINE_TO);
        vertices.add(current = relative ? current.add(position) : position);
        previousControl = current;
    }

    /**
     * draws a bezier curve to the position with one control point
     * @param control the control point (relative to the cursor)
     * @param position the position
     * @param relative defines if position is relative
     */
    public void curveTo(Vec2 control, Vec2 position, boolean relative) {
        commands.add(CURVE_TO_QUADRATIC);
        vertices.add(previousControl = relative ? current.add(control) : control);
        vertices.add(current = relative ? current.add(position) : position);
    }

    /**
     * draws a bezier curve to the position with one control point generated from the last known control point
     * @param position the position
     * @param relative defines if position is relative
     */
    public void curveToSmooth(Vec2 position, boolean relative) {
        Vec2 control = new Vec2(current.x * 2f - previousControl.x, current.y * 2f - previousControl.y);
        curveTo(control, position, relative);
    }

    /**
     * draws a bezier curve to the position with 2 control points
     * @param control1 first control point
     * @param control2 second control point
     * @param position the position
     * @param relative defines if the position is relative
     */
    public void curveTo(Vec2 control1, Vec2 control2, Vec2 position, boolean relative) {
        commands.add(CURVE_TO_CUBIC);
        vertices.add(relative ? current.add(control1) : control1);
        vertices.add(previousControl = relative ? current.add(control2) : control2);
        vertices.add(current = relative ? current.add(position) : position);
    }

    /**
     * draws a bezier curve to the position with 2 control points. The first one is generated from the last known control point
     * @param control2 second control point
     * @param position the position
     * @param relative defines if the position is relative
     */
    public void curveToSmooth(Vec2 control2, Vec2 position, boolean relative) {
        Vec2 control1 = new Vec2(current.x * 2f - previousControl.x, current.y * 2f - previousControl.y);
        curveTo(control1, control2, position, relative);
    }


    public void closePath() {
        if(commands.isEmpty() || commands.get(commands.size() - 1) != CLOSE) {
            commands.add(CLOSE);
            current = previousControl = previousMove;
        }
    }

    public static final class SVGParser {
        public final String svgpath;
        public final int len;
        public int pos;
        public boolean allowcomma;

        public SVGParser(String svgpath) {
            this.svgpath = svgpath;
            this.len = svgpath.length();
            this.allowcomma = false;
        }

        public boolean isDone() {
            return (toNextNonWhitespace() >= len);
        }

        public char getChar() {
            return svgpath.charAt(pos++);
        }

        public boolean nextIsNumber() {
            if (toNextNonWhitespace() < len) {
                switch (svgpath.charAt(pos)) {
                    case '-':
                    case '+':
                    case '0': case '1': case '2': case '3': case '4':
                    case '5': case '6': case '7': case '8': case '9':
                    case '.':
                        return true;
                }
            }
            return false;
        }

        public Vec2 vec2H() {
            return new Vec2(0, f());
        }

        public Vec2 vec2V() {
            return new Vec2(f(), 0);
        }

        public Vec2 vec2() {
            return new Vec2(f(), f());
        }

        public float f() {
            return getFloat();
        }

        public float a() {
            return (float) Math.toRadians(getFloat());
        }

        public float getFloat() {
            int start = toNextNonWhitespace();
            this.allowcomma = true;
            int end = toNumberEnd();
            if (start < end) {
                String flstr = svgpath.substring(start, end);
                try {
                    return Float.parseFloat(flstr);
                } catch (NumberFormatException e) {
                }
                throw new IllegalArgumentException("invalid float ("+flstr+ ") in path at pos="+start);
            }
            throw new IllegalArgumentException("end of path looking for float");
        }

        public boolean b() {
            toNextNonWhitespace();
            this.allowcomma = true;
            if (pos < len) {
                char flag = svgpath.charAt(pos);
                switch (flag) {
                    case '0': pos++; return false;
                    case '1': pos++; return true;
                }
                throw new IllegalArgumentException("invalid boolean flag ("+flag+ ") in path at pos="+pos);
            }
            throw new IllegalArgumentException("end of path looking for boolean");
        }

        private int toNextNonWhitespace() {
            boolean canbecomma = this.allowcomma;
            while (pos < len) {
                switch (svgpath.charAt(pos)) {
                    case ',':
                        if (!canbecomma) {
                            return pos;
                        }
                        canbecomma = false;
                        break;
                    case ' ':
                    case '\t':
                    case '\r':
                    case '\n':
                        break;
                    default:
                        return pos;
                }
                pos++;
            }
            return pos;
        }

        private int toNumberEnd() {
            boolean allowsign = true;
            boolean hasexp = false;
            boolean hasdecimal = false;
            while (pos < len) {
                switch (svgpath.charAt(pos)) {
                    case '-':
                    case '+':
                        if (!allowsign) return pos;
                        allowsign = false;
                        break;
                    case '0': case '1': case '2': case '3': case '4':
                    case '5': case '6': case '7': case '8': case '9':
                        allowsign = false;
                        break;
                    case 'E': case 'e':
                        if (hasexp) return pos;
                        hasexp = allowsign = true;
                        break;
                    case '.':
                        if (hasexp || hasdecimal) return pos;
                        hasdecimal = true;
                        allowsign = false;
                        break;
                    default:
                        return pos;
                }
                pos++;
            }
            return pos;
        }
    }
}