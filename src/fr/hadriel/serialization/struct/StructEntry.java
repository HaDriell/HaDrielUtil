package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public abstract class StructEntry {

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
    public static final byte TYPE_LIST      = 11;
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
            case TYPE_LIST: return "StList";
            case TYPE_OBJECT: return "StObject";
        }
        return "UNKNOWN";
    }

    private byte dataType;

    protected StructEntry(byte type) {
        this.dataType = type;
    }

    public int getSize() {
        return 1 + getSizeImpl();
    }

    public int serialize(byte[] buffer, int pointer) {
        pointer = Serial.write(buffer, pointer, dataType);
        return serializeImpl(buffer, pointer);
    }

    protected abstract int getSizeImpl();

    protected abstract int serializeImpl(byte[] buffer, int pointer);

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
            case TYPE_LIST: return StList.deserialize(buffer, pointer);
            case TYPE_OBJECT: return StObject.deserialize(buffer, pointer);
        }
        throw new RuntimeException(String.format("Corrupt buffer or unknown type 0x%x", b));
    }
}