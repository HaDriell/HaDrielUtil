package fr.hadriel.serialization.struct;

/**
 * Created by glathuiliere on 19/08/2016.
 */
public final class Struct {

    //Primitives
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
    public static final byte TYPE_ARRAY = 11;
    public static final byte TYPE_OBJECT    = 12;

    public static String getTypeName(byte type) {
        switch (type) {
            case TYPE_BOOL: return "StByte";
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

    public static StructEntry deserialize(byte[] buffer, int pointer) {
        byte b = buffer[pointer];
        switch (b) {
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
        throw new RuntimeException(String.format("Corrupt buffer or unknown type 0x%x", b));
    }
}
