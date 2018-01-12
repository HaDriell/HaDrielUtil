package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 *
 * @author glathuiliere
 */
public class CollectionSerializer<Element> implements ISerializer<Collection<Element>> {

    /**
     * Functional interface to delegate the collection instanciation during deserialization
     * @param <E> element type
     */
    @FunctionalInterface
    public static interface ICollectionProvider<E> {
        public Collection<E> provide(int minSize);
    }

    private final Class<Element> type;
    private final ICollectionProvider<Element> provider;


    public CollectionSerializer(Class<Element> type) {
        this(ArrayList::new, type);
    }

    public CollectionSerializer(ICollectionProvider<Element> provider, Class<Element> type) {
        this.type = type;
        this.provider = Objects.requireNonNull(provider);
    }

    public Collection<Element> deserialize(Serialization serialization, Buffer buffer) {
        int size = buffer.readInt();
        Collection<Element> collection = provider.provide(size);
        for(int i = 0; i < size; i++)
            collection.add(type.cast(serialization.deserialize(buffer)));
        return collection;
    }

    public void serialize(Serialization serialization, Buffer buffer, Collection<Element> collection) {
        buffer.write(collection.size());
        for(Element element : collection) {
            serialization.serialize(buffer, element);
        }
    }

    public int sizeof(Serialization serialization, Collection<Element> collection) {
        int size = 4; // array.length
        for(Element element : collection)
            size += serialization.sizeof(element);
        return size;
    }
}