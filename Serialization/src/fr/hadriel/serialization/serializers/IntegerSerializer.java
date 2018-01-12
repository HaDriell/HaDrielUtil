package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class IntegerSerializer implements ISerializer<Integer> {

    public Integer deserialize(Serialization serialization, Buffer buffer) {
        return buffer.readInt();
    }

    public void serialize(Serialization serialization, Buffer buffer, Integer object) {
        buffer.write(object);
    }

    public int sizeof(Serialization serialization, Integer instance) {
        return 4;
    }
}
