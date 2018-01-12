package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class ShortSerializer implements ISerializer<Short> {

    public Short deserialize(Serialization serialization, Buffer buffer) {
        return buffer.readShort();
    }

    public void serialize(Serialization serialization, Buffer buffer, Short object) {
        buffer.write(object);
    }

    public int sizeof(Serialization serialization, Short instance) {
        return 2;
    }
}
