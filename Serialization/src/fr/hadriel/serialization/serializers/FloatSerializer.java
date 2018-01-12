package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class FloatSerializer implements ISerializer<Float> {

    public Float deserialize(Serialization serialization, Buffer buffer) {
        return buffer.readFloat();
    }

    public void serialize(Serialization serialization, Buffer buffer, Float object) {
        buffer.write(object);
    }

    public int sizeof(Serialization serialization, Float instance) {
        return 4;
    }
}
