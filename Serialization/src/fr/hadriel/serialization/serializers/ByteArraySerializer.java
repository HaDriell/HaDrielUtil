package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class ByteArraySerializer implements ISerializer<byte[]> {

    public byte[] deserialize(Serialization serialization, Buffer buffer) {
        int length = buffer.readInt();
        byte[] array = new byte[length];
        for(int i = 0; i < array.length; i++)
            array[i] = buffer.readByte();
        return array;
    }

    public void serialize(Serialization serialization, Buffer buffer, byte[] array) {
        buffer.write(array.length);
        for(byte b : array)
            buffer.write(b);
    }
}