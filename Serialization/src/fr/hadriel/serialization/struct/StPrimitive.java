package fr.hadriel.serialization.struct;

import fr.hadriel.serialization.IPrimitive;
import fr.hadriel.serialization.Serial;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public abstract class StPrimitive implements IPrimitive {

    private byte dataType;

    protected StPrimitive(byte type) {
        this.dataType = type;
    }

    public int getSize() {
        return 1 + getDataSize();
    }

    public int serialize(byte[] buffer, int pointer) {
        pointer = Serial.write(buffer, pointer, dataType);
        return serializeData(buffer, pointer);
    }

    protected abstract int getDataSize();

    protected abstract int serializeData(byte[] buffer, int pointer);

    public abstract StArray asStArray();

    public abstract StObject asStObject();

    public abstract boolean equals(StPrimitive primitive);

}