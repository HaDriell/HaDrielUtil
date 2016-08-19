package fr.hadriel.serialization.json;

/**
 * Created by glathuiliere on 19/08/2016.
 */
public class JsPrimitiv implements JsonType {

    private Object value;

    public JsPrimitiv(Object value) {
        set(value);
    }

    public void set(Object value) {
        Json.checkJsonValidPrimitiveType(value);
        this.value = value;
    }

    public Object get() {
        return value;
    }

    public boolean isNull() {
        return value == null;
    }

    public boolean isBool() {
        return value instanceof Boolean;
    }

    public boolean isNumber() {
        return value instanceof Byte ||
                value instanceof Short ||
                value instanceof Character ||
                value instanceof Integer ||
                value instanceof Long ||
                value instanceof Float ||
                value instanceof Double;
    }

    public boolean isString() {
        return value instanceof String;
    }

    public String toString() {
        if(isNull()) return "null";
        return isString() ? '"' + value.toString() + '"' : value.toString();
    }
}