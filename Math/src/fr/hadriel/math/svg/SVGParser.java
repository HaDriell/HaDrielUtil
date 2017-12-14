package fr.hadriel.math.svg;

import fr.hadriel.math.Vec2;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public final class SVGParser {
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
