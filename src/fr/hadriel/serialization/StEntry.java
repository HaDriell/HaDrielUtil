package fr.hadriel.serialization;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public abstract class StEntry {

    //Primitives
    public static final byte TYPE_BOOL      = 0x01;
    public static final byte TYPE_BYTE      = 0x02;
    public static final byte TYPE_SHORT     = 0x03;
    public static final byte TYPE_CHAR      = 0x04;
    public static final byte TYPE_INT       = 0x05;
    public static final byte TYPE_FLOAT     = 0x06;
    public static final byte TYPE_LONG      = 0x07;
    public static final byte TYPE_DOUBLE    = 0x08;
    public static final byte TYPE_STRING    = 0x09;

    //Structures
    public static final byte TYPE_LIST      = 0x11;
    public static final byte TYPE_OBJECT    = 0x12;

    private byte dataType;

    protected StEntry(byte type) {
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

    public static StEntry deserialize(byte[] buffer, int pointer) {
        byte b = buffer[pointer];
        System.out.println(b);
        switch (b) {
            case TYPE_BOOL: return StBool.deserialize(buffer, pointer);
            case TYPE_BYTE: return StByte.deserialize(buffer, pointer);
            case TYPE_SHORT: return StShort.deserialize(buffer, pointer);
            case TYPE_CHAR: return StChar.deserialize(buffer, pointer);
            case TYPE_INT: return StInt.deserialize(buffer, pointer);
            case TYPE_FLOAT: return StFloat.deserialize(buffer, pointer);
            case TYPE_LONG: return StLong.deserialize(buffer, pointer);
            case TYPE_DOUBLE: return StDouble.deserialize(buffer, pointer);
            case TYPE_STRING: return StString.deserialize(buffer, pointer);
            case TYPE_LIST: return StList.deserialize(buffer, pointer);
        }
        throw new RuntimeException("Corrupt buffer");
    }
}