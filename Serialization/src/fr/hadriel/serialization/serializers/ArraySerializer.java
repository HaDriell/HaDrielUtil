package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

import java.lang.reflect.Array;
import java.util.Objects;

/**
 *
 * @author glathuiliere
 */
public class ArraySerializer<Element> implements ISerializer<Element[]> {

    @FunctionalInterface
    private static interface IArrayProvider<E> {
        public E[] provide(int size);
    }

    private final Class<Element> type;
    private final IArrayProvider<Element> provider;

    @SuppressWarnings("unchecked")
    public ArraySerializer(Class<Element> type) {
        this(type, (size) -> (Element[]) Array.newInstance(type, size));
    }

    public ArraySerializer(Class<Element> type, IArrayProvider<Element> provider) {
        this.type = type;
        this.provider = Objects.requireNonNull(provider);
    }

    public Element[] deserialize(Serialization serialization, Buffer buffer) {
        int size = buffer.readInt();
        Element[] array = provider.provide(size); // holy shit that's magic !
        for(int i = 0; i < size; i++)
            array[i] = type.cast(serialization.deserialize(buffer));
        return array;
    }

    public void serialize(Serialization serialization, Buffer buffer, Element[] array) {
        buffer.write(array.length);
        for(Element element : array)
            serialization.serialize(buffer, element);
    }
}