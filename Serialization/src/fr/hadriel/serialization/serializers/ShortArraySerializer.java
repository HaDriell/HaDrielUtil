package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class ShortArraySerializer implements ISerializer<short[]> {

    public short[] deserialize(Serialization serialization, Buffer buffer) {
        int length = buffer.readInt();
        short[] array = new short[length];
        for(int i = 0; i < array.length; i++)
            array[i] = buffer.readShort();
        return array;
    }

    public void serialize(Serialization serialization, Buffer buffer, short[] array) {
        buffer.write(array.length);
        for(short b : array)
            buffer.write(b);
    }

    public int sizeof(Serialization serialization, short[] instance) {
        return 4 + instance.length * 2;
    }
}