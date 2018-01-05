package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author glathuiliere
 */
public class MapSerializer<Key, Value> implements ISerializer<Map<Key, Value>> {

    /**
     * Functional interface to delegate map instanciation during deserialization
     * @param <K> key type
     * @param <V> value type
     */
    @FunctionalInterface
    public static interface IMapProvider<K, V> {
        public Map<K, V> provide(int minSize);
    }

    private final Class<Key> typeKey;
    private final Class<Value> typeValue;
    private final IMapProvider<Key, Value> provider;

    public MapSerializer(Class<Key> typeKey, Class<Value> typeValue) {
        this(typeKey, typeValue, (size) -> new HashMap<>());
    }

    public MapSerializer(Class<Key> typeKey, Class<Value> typeValue, IMapProvider<Key, Value> provider) {
        this.typeKey = typeKey;
        this.typeValue = typeValue;
        this.provider = Objects.requireNonNull(provider);
    }

    public Map<Key, Value> deserialize(Serialization serialization, Buffer buffer) {
        int size = buffer.readInt();
        Map<Key, Value> map = provider.provide(size);
        for(int i = 0; i < size; i++) {
            Key key = typeKey.cast(serialization.deserialize(buffer));
            Value value = typeValue.cast(serialization.deserialize(buffer));
            map.put(key, value);
        }
        return map;
    }

    public void serialize(Serialization serialization, Buffer buffer, Map<Key, Value> map) {
        buffer.write(map.size());
        for(Map.Entry<Key, Value> entry : map.entrySet()) {
            serialization.serialize(buffer, entry.getKey());
            serialization.serialize(buffer, entry.getValue());
        }
    }
}