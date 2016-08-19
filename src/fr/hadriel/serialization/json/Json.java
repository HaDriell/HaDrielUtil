package fr.hadriel.serialization.json;

import java.nio.ByteBuffer;

/**
 * Created by glathuiliere on 19/08/2016.
 */
public final class Json {

    private static final Class[] JSON_PRIMITIVE_TYPES = {
            Byte.class,
            Boolean.class,
            Short.class,
            Character.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            String.class
    };

    public static boolean isJsonValidType(Object reference) {
        return JsonType.class.isInstance(reference) || isJsonValidPrimitiveType(reference);
    }

    public static boolean isJsonValidPrimitiveType(Object reference) {
        if(reference == null) return true;
        for(Class type : JSON_PRIMITIVE_TYPES) {
            if(type == reference.getClass())
                return true;
        }
        return false;
    }

    public static void checkJsonValidPrimitiveType(Object reference) {
        if(!isJsonValidPrimitiveType(reference))
            throw new IllegalArgumentException("Invalid JSON Primitive Type");
    }

    public static void checkJsonValidType(Object reference) {
        if(!isJsonValidType(reference))
            throw new IllegalArgumentException("Invalid JSON Type");
    }

    public static JsonType parse(ByteBuffer buffer) {
        return null;
    }

    private static JsObject parseObject(ByteBuffer buffer) {
        JsObject object = new JsObject();
        byte b;
        buffer.get(); // '{'
        boolean done = false;
        while(!done) {
            b = buffer.get();
            buffer.position(buffer.position() - 1); // rollback

            if(Character.isWhitespace(b)) {
                return null;
            } else if(b == ',') {
                buffer.get(); // advance
            } else if(b == '}') {
                buffer.get();
                return object;
            } else {
                //TODO
            }
        }

        return object;
    }
}