package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class CharacterArraySerializer implements ISerializer<char[]> {

    public char[] deserialize(Serialization serialization, Buffer buffer) {
        int length = buffer.readInt();
        char[] array = new char[length];
        for(int i = 0; i < array.length; i++)
            array[i] = buffer.readChar();
        return array;
    }

    public void serialize(Serialization serialization, Buffer buffer, char[] array) {
        buffer.write(array.length);
        for(char b : array)
            buffer.write(b);
    }

    public int sizeof(Serialization serialization, char[] instance) {
        return 4 + instance.length * 2;
    }
}