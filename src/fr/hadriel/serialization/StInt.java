package fr.hadriel.serialization;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StInt extends StEntry {

    public int value;

    public StInt(int value) {
        super(TYPE_INT);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 4;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StInt deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != TYPE_INT) return null;
        pointer++;
        int value = Serial.readInt(buffer, pointer);
        return new StInt(value);
    }
}
