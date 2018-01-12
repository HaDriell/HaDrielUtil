package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class IntegerArraySerializer implements ISerializer<int[]> {

    public int[] deserialize(Serialization serialization, Buffer buffer) {
        int length = buffer.readInt();
        int[] array = new int[length];
        for(int i = 0; i < array.length; i++)
            array[i] = buffer.readInt();
        return array;
    }

    public void serialize(Serialization serialization, Buffer buffer, int[] array) {
        buffer.write(array.length);
        for(int b : array)
            buffer.write(b);
    }

    public int sizeof(Serialization serialization, int[] instance) {
        return 4 + instance.length * 4;
    }
}