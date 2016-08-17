package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.Serial;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class StChar extends StructEntry {

    public char value;

    public StChar(char value) {
        super(TYPE_CHAR);
        this.value = value;
    }

    protected int getSizeImpl() {
        return 2;
    }

    protected int serializeImpl(byte[] buffer, int pointer) {
        return Serial.write(buffer, pointer, value);
    }

    public static StChar deserialize(byte[] buffer, int pointer) {
        if(buffer[pointer] != TYPE_CHAR) return null;
        pointer++;
        char value = Serial.readChar(buffer, pointer);
        return new StChar(value);
    }

    public String toString() {
        return "StChar(" + value + ")";
    }
}
