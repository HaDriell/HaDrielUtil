package fr.hadriel.serialization;

import fr.hadriel.util.Buffer;

/**
 * Created by gauti on 19/12/2017.
 */
public interface IDeserializer<T> {
    public T deserialize(Buffer buffer);
}