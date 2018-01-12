package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serial;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by glathuiliere on 19/08/2016.
 */
public final class Struct {
    private Struct() {} // cannot instanciate

    public static class StructSerializer<StType extends StPrimitive> implements ISerializer<StType> {

        @SuppressWarnings("unchecked")
        public StType deserialize(Serialization serialization, Buffer buffer) {
            int pointer = buffer.position();
            byte[] array = buffer.array();
            StType primitive = (StType) Struct.deserialize(array, pointer);
            buffer.position(pointer + primitive.getSize());
            return primitive;
        }

        public void serialize(Serialization serialization, Buffer buffer, StType object) {
            int pointer = buffer.position();
            byte[] array = buffer.array();
            pointer = object.serialize(array, pointer);
            buffer.position(pointer);
        }

        public int sizeof(Serialization serialization, StType instance) {
            return instance.getSize();
        }
    }

    private static final StructSerializer<StBoolean> BOOLEAN_SERIALIZER = new StructSerializer<>();
    private static final StructSerializer<StByte> BYTE_SERIALIZER = new StructSerializer<>();
    private static final StructSerializer<StShort> SHORT_SERIALIZER = new StructSerializer<>();
    private static final StructSerializer<StChar> CHAR_SERIALIZER = new StructSerializer<>();
    private static final StructSerializer<StInt> INT_SERIALIZER = new StructSerializer<>();
    private static final StructSerializer<StLong> LONG_SERIALIZER = new StructSerializer<>();
    private static final StructSerializer<StFloat> FLOAT_SERIALIZER = new StructSerializer<>();
    private static final StructSerializer<StDouble> DOUBLE_SERIALIZER = new StructSerializer<>();
    private static final StructSerializer<StString> STRING_SERIALIZER = new StructSerializer<>();
    private static final StructSerializer<StArray> ARRAY_SERIALIZER = new StructSerializer<>();
    private static final StructSerializer<StObject> OBJECT_SERIALIZER = new StructSerializer<>();

    public static void registerStructSerializers(Serialization serialization) {
        serialization.register(StArray.class, ARRAY_SERIALIZER);
        serialization.register(StObject.class, OBJECT_SERIALIZER);
        serialization.register(StBoolean.class, BOOLEAN_SERIALIZER);
        serialization.register(StByte.class, BYTE_SERIALIZER);
        serialization.register(StShort.class, SHORT_SERIALIZER);
        serialization.register(StChar.class, CHAR_SERIALIZER);
        serialization.register(StInt.class, INT_SERIALIZER);
        serialization.register(StLong.class, LONG_SERIALIZER);
        serialization.register(StFloat.class, FLOAT_SERIALIZER);
        serialization.register(StDouble.class, DOUBLE_SERIALIZER);
        serialization.register(StString.class, STRING_SERIALIZER);
    }

    private static class StNull extends StPrimitive {

        private StNull() {
            super(TYPE_NULL);
        }

        protected int getDataSize() {
            return 0;
        }

        protected int serializeData(byte[] buffer, int pointer) {
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

        public boolean equals(StPrimitive primitive) {
            return primitive instanceof StNull;
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
