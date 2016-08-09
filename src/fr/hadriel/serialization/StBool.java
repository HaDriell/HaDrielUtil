package fr.hadriel.serialization;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StBool extends StEntry {

    public boolean value;

    public StBool(boolean value) {
        super(TYPE_BOOL);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 1;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StBool deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != TYPE_BOOL) return null;
        pointer++;
        boolean value = Serial.readBoolean(buffer, pointer);
        return new StBool(value);
    }
}
