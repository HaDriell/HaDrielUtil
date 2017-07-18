package fr.hadriel.main.serialization.js;

import fr.hadriel.main.serialization.SerialException;

/**
 * Created by HaDriel on 19/10/2016.
 */
public class JsNull implements JsPrimitive {

    public byte asByte() {
        throw new UnsupportedOperationException();
    }

    public boolean asBoolean() {
        throw new UnsupportedOperationException();
    }

    public short asShort() {
        throw new UnsupportedOperationException();
    }

    public char asChar() {
        throw new UnsupportedOperationException();
    }

    public int asInt() {
        throw new UnsupportedOperationException();
    }

    public long asLong() {
        throw new UnsupportedOperationException();
    }

    public float asFloat() {
        throw new UnsupportedOperationException();
    }

    public double asDouble() {
        throw new UnsupportedOperationException();
    }

    public String asString() {
        return "null";
    }

    public String toString() {
        return asString();
    }

    public static JsNull deserialize(String input) throws SerialException {
        if(!input.equals("null")) throw new SerialException("excpected 'null', found " + input);
        return new JsNull();
    }
}
