package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class StringSerializer implements ISerializer<String> {

    public String deserialize(Serialization serialization, Buffer buffer) {
        int length = buffer.readInt();
        byte[] array = new byte[length];
        for(int i = 0; i < array.length; i++)
            array[i] = buffer.readByte();
        return new String(array);
    }

    public void serialize(Serialization serialization, Buffer buffer, String string) {
        buffer.write(string.length());
        for(byte b : string.getBytes())
            buffer.write(b);
    }

    public int sizeof(Serialization serialization, String instance) {
        return 4 + instance.getBytes().length;
    }
}