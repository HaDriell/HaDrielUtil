package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class DoubleArraySerializer implements ISerializer<double[]> {

    public double[] deserialize(Serialization serialization, Buffer buffer) {
        int length = buffer.readInt();
        double[] array = new double[length];
        for(int i = 0; i < array.length; i++)
            array[i] = buffer.readDouble();
        return array;
    }

    public void serialize(Serialization serialization, Buffer buffer, double[] array) {
        buffer.write(array.length);
        for(double b : array)
            buffer.write(b);
    }
}