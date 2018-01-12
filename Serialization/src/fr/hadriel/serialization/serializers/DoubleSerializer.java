package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class DoubleSerializer implements ISerializer<Double> {

    public Double deserialize(Serialization serialization, Buffer buffer) {
        return buffer.readDouble();
    }

    public void serialize(Serialization serialization, Buffer buffer, Double object) {
        buffer.write(object);
    }

    public int sizeof(Serialization serialization, Double instance) {
        return 8;
    }
}
