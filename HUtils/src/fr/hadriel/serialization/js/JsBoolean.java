package fr.hadriel.serialization.js;

import fr.hadriel.serialization.Serial;
import fr.hadriel.serialization.SerialException;

/**
 * Created by HaDriel on 19/10/2016.
 */
public class JsBoolean implements JsPrimitive {

    public boolean value;

    public JsBoolean(boolean value) {
        this.value = value;
    }

    public byte asByte() {
        return value ? Serial.TRUE : Serial.FALSE;
    }

    public boolean asBoolean() {
        return value;
    }

    public short asShort() {
        return value ? Serial.TRUE : Serial.FALSE;
    }

    public char asChar() {
        return (char) (value ? Serial.TRUE : Serial.FALSE);
    }

    public int asInt() {
        return value ? Serial.TRUE : Serial.FALSE;
    }

    public long asLong() {
        return value ? Serial.TRUE : Serial.FALSE;
    }

    public float asFloat() {
        return value ? Serial.TRUE : Serial.FALSE;
    }

    public double asDouble() {
        return value ? Serial.TRUE : Serial.FALSE;
    }

    public String asString() {
        return Boolean.toString(value);
    }

    public String toString() {
        return asString();
    }

    public static JsBoolean deserialize(String input) throws SerialException {
        if(input.equals("true")) return new JsBoolean(true);
        if(input.equals("false")) return new JsBoolean(false);
        throw new SerialException("Excpected 'true' or 'false' but found " + input);
    }
}