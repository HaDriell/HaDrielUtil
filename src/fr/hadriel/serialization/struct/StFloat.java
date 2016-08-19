package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StFloat extends StructEntry {

    public float value;

    public StFloat(float value) {
        super(Struct.TYPE_FLOAT);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 4;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StFloat deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != Struct.TYPE_FLOAT) return null;
        pointer++;
        float value = Serial.readFloat(buffer, pointer);
        return new StFloat(value);
    }

    public String toString() {
        return "StFloat(" + value + ")";
    }
}
