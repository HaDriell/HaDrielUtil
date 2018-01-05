package fr.hadriel.serialization;

import fr.hadriel.util.Buffer;

/**
 * Created by gauti on 19/12/2017.
 */
public interface ISerializer<T> {

    public T deserialize(Serialization serialization, Buffer buffer);
    public void serialize(Serialization serialization, Buffer buffer, T object);
}