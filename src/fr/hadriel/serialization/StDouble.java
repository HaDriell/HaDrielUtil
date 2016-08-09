package fr.hadriel.serialization;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StDouble extends StEntry {

    public double value;

    public StDouble(double value) {
        super(TYPE_DOUBLE);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 8;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StDouble deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != TYPE_DOUBLE) return null;
        pointer++;
        double value = Serial.readDouble(buffer, pointer);
        return new StDouble(value);
    }
}
