package fr.hadriel.serialization.jsonfixed;

import fr.hadriel.serialization.SerialException;

/**
 * Created by HaDriel setOn 19/10/2016.
 */
public class JsString implements JsPrimitive {

    public String value;

    public JsString(String value) {
        this.value = value;
    }

    public byte asByte() {
        return Byte.parseByte(value);
    }

    public boolean asBoolean() {
        return Boolean.parseBoolean(value);
    }

    public short asShort() {
        return Short.parseShort(value);
    }

    public char asChar() {
        return value.charAt(0);
    }

    public int asInt() {
        return Integer.parseInt(value);
    }

    public long asLong() {
        return Long.parseLong(value);
    }

    public float asFloat() {
        return Float.parseFloat(value);
    }

    public double asDouble() {
        return Double.parseDouble(value);
    }

    public String asString() {
        return "\"" + value + "\"";
    }

    public String toString() {
        return asString();
    }

    public static JsString deserialize(String input) throws SerialException {
        return new JsString(Json.getJsonStringValue(input));
    }
}