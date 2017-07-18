package fr.hadriel.main.serialization.struct;

import fr.hadriel.main.serialization.Serial;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by glathuiliere on 19/08/2016.
 */
public final class Struct {
    private Struct() {} // cannot instanciate

    private static class StNull extends StPrimitive {

        private StNull() {
            super(TYPE_NULL);
        }

        protected int getSizeImpl() {
            return 0;
        }

        protected int serializeImpl(byte[] buffer, int pointer) {
            return pointer;
        }

        public StArray asStArray() {
            throw new UnsupportedOperationException();
        }

        public StObject asStObject() {
            throw new UnsupportedOperationException();
        }

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
            throw new UnsupportedOperationException();
        }

        public String toString() {
            return "null";
        }
    }

    //Special value for null references
    public static final StNull NULL = new StNull();

    //Primitives
    public static final byte TYPE_NULL      = 0;
    public static final byte TYPE_BOOL      = 1;
    public static final byte TYPE_BYTE      = 2;
    public static final byte TYPE_SHORT     = 3;
    public static final byte TYPE_CHAR      = 4;
    public static final byte TYPE_INT       = 5;
    public static final byte TYPE_FLOAT     = 6;
    public static final byte TYPE_LONG      = 7;
    public static final byte TYPE_DOUBLE    = 8;
    public static final byte TYPE_STRING    = 9;

    //Structures
    public static final byte TYPE_ARRAY     = 11;
    public static final byte TYPE_OBJECT    = 12;

    public static String getTypeName(byte type) {
        switch (type) {
            case TYPE_NULL: return "StNull";
            case TYPE_BOOL: return "StBool";
            case TYPE_BYTE: return "StByte";
            case TYPE_SHORT: return "StShort";
            case TYPE_CHAR: return "StChar";
            case TYPE_INT: return "StInt";
            case TYPE_FLOAT: return "StFloat";
            case TYPE_LONG: return "StLong";
            case TYPE_DOUBLE: return "StDouble";
            case TYPE_STRING: return "StString";
            case TYPE_ARRAY: return "StArray";
            case TYPE_OBJECT: return "StObject";
        }
        return "UNKNOWN";
    }

    public static StPrimitive deserialize(InputStream in) throws IOException {
        byte b = Serial.readByte(in);
        switch (b) {
            case TYPE_NULL: return NULL;
            case TYPE_BOOL: return StBoolean.deserialize(b, in);
            case TYPE_BYTE: return StByte.deserialize(b, in);
            case TYPE_SHORT: return StShort.deserialize(b, in);
            case TYPE_CHAR: return StChar.deserialize(b, in);
            case TYPE_INT: return StInt.deserialize(b, in);
            case TYPE_FLOAT: return StFloat.deserialize(b, in);
            case TYPE_LONG: return StLong.deserialize(b, in);
            case TYPE_DOUBLE: return StDouble.deserialize(b, in);
            case TYPE_STRING: return StString.deserialize(b, in);
            case TYPE_ARRAY: return StArray.deserialize(b, in);
            case TYPE_OBJECT: return StObject.deserialize(b, in);
        }
        throw new RuntimeException(String.format("Corrupt buffer or unknown name 0x%x", b));
    }

    public static StPrimitive deserialize(byte[] buffer, int pointer) {
        byte b = buffer[pointer];
        switch (b) {
            case TYPE_NULL: return NULL;
            case TYPE_BOOL: return StBoolean.deserialize(buffer, pointer);
            case TYPE_BYTE: return StByte.deserialize(buffer, pointer);
            case TYPE_SHORT: return StShort.deserialize(buffer, pointer);
            case TYPE_CHAR: return StChar.deserialize(buffer, pointer);
            case TYPE_INT: return StInt.deserialize(buffer, pointer);
            case TYPE_FLOAT: return StFloat.deserialize(buffer, pointer);
            case TYPE_LONG: return StLong.deserialize(buffer, pointer);
            case TYPE_DOUBLE: return StDouble.deserialize(buffer, pointer);
            case TYPE_STRING: return StString.deserialize(buffer, pointer);
            case TYPE_ARRAY: return StArray.deserialize(buffer, pointer);
            case TYPE_OBJECT: return StObject.deserialize(buffer, pointer);
        }
        throw new RuntimeException(String.format("Corrupt buffer or unknown name 0x%x", b));
    }
}
