package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

/**
 *
 * @author glathuiliere
 */
public class CharacterSerializer implements ISerializer<Character> {

    public Character deserialize(Serialization serialization, Buffer buffer) {
        return buffer.readChar();
    }

    public void serialize(Serialization serialization, Buffer buffer, Character object) {
        buffer.write(object);
    }
}