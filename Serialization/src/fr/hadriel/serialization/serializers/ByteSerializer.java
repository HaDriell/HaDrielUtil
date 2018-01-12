package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class ByteSerializer implements ISerializer<Byte> {

    public Byte deserialize(Serialization serialization, Buffer buffer) {
        return buffer.readByte();
    }

    public void serialize(Serialization serialization, Buffer buffer, Byte object) {
        buffer.write(object);
    }

    public int sizeof(Serialization serialization, Byte instance) {
        return 1;
    }
}
