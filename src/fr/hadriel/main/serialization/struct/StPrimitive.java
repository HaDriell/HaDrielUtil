package fr.hadriel.main.serialization.struct;

import fr.hadriel.main.serialization.IPrimitive;
import fr.hadriel.main.serialization.Serial;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public abstract class StPrimitive implements IPrimitive {

    private byte dataType;

    protected StPrimitive(byte type) {
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

    public abstract StArray asStArray();

    public abstract StObject asStObject();

}