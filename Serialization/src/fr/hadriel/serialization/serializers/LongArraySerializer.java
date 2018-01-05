package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class LongArraySerializer implements ISerializer<long[]> {

    public long[] deserialize(Serialization serialization, Buffer buffer) {
        int length = buffer.readInt();
        long[] array = new long[length];
        for(int i = 0; i < array.length; i++)
            array[i] = buffer.readLong();
        return array;
    }

    public void serialize(Serialization serialization, Buffer buffer, long[] array) {
        buffer.write(array.length);
        for(long b : array)
            buffer.write(b);
    }
}