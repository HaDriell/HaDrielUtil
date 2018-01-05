package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class BooleanSerializer implements ISerializer<Boolean> {

    public Boolean deserialize(Serialization serialization, Buffer buffer) {
        return buffer.readBoolean();
    }

    public void serialize(Serialization serialization, Buffer buffer, Boolean object) {
        buffer.write(object);
    }
}
