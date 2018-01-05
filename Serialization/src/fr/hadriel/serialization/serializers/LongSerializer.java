package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class LongSerializer implements ISerializer<Long> {

    public Long deserialize(Serialization serialization, Buffer buffer) {
        return buffer.readLong();
    }

    public void serialize(Serialization serialization, Buffer buffer, Long object) {
        buffer.write(object);
    }
}
