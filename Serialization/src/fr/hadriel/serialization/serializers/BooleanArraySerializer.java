package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class BooleanArraySerializer implements ISerializer<boolean[]> {

    public boolean[] deserialize(Serialization serialization, Buffer buffer) {
        int length = buffer.readInt();
        boolean[] array = new boolean[length];
        for(int i = 0; i < array.length; i++)
            array[i] = buffer.readBoolean();
        return array;
    }

    public void serialize(Serialization serialization, Buffer buffer, boolean[] array) {
        buffer.write(array.length);
        for(boolean b : array)
            buffer.write(b);
    }

    public int sizeof(Serialization serialization, boolean[] instance) {
        return 4 + instance.length;
    }
}