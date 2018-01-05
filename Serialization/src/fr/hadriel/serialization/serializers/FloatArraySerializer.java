package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class FloatArraySerializer implements ISerializer<float[]> {

    public float[] deserialize(Serialization serialization, Buffer buffer) {
        int length = buffer.readInt();
        float[] array = new float[length];
        for(int i = 0; i < array.length; i++)
            array[i] = buffer.readFloat();
        return array;
    }

    public void serialize(Serialization serialization, Buffer buffer, float[] array) {
        buffer.write(array.length);
        for(float b : array)
            buffer.write(b);
    }
}