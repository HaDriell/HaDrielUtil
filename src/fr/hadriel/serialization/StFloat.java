package fr.hadriel.serialization;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StFloat extends StEntry {

    public float value;

    public StFloat(float value) {
        super(TYPE_FLOAT);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 4;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StFloat deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != TYPE_FLOAT) return null;
        pointer++;
        float value = Serial.readFloat(buffer, pointer);
        return new StFloat(value);
    }
}
