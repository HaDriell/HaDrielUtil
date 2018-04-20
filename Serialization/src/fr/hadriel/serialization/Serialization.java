package fr.hadriel.serialization;

import fr.hadriel.serialization.serializers.*;
import fr.hadriel.util.Buffer;
import fr.hadriel.util.typedef.TypeDefinitionException;
import fr.hadriel.util.typedef.TypeResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author glathuiliere
 */
public class Serialization {

    //Null normalization over Serialization
    private static final class NullValue {
        public static final class Serializer implements ISerializer<NullValue> {
            public NullValue deserialize(Serialization serialization, Buffer buffer) { return null; } // doesn't read anything
            public void serialize(Serialization serialization, Buffer buffer, NullValue object) { } // doesn't write anything
            public int sizeof(Serialization serialization, NullValue instance) { return 0; }
        }
    }
    private static final NullValue NULL = new NullValue(); // value replacer



    private TypeResolver types;
    private Map<Class, ISerializer> serializers;

    public Serialization() {
        this(true);
    }

    public Serialization(boolean loadDefaults) {
        this.types = new TypeResolver();
        this.serializers = new HashMap<>();
        register(NullValue.class, new NullValue.Serializer()); // always use the first type registered as the NullValue serialization
        if(loadDefaults)
            loadDefaults();
    }

    private void loadDefaults() {
        //Java Primitives
        register(Boolean.class, new BooleanSerializer());
        register(Byte.class, new ByteSerializer());
        register(Short.class, new ShortSerializer());
        register(Character.class, new CharacterSerializer());
        register(Integer.class, new IntegerSerializer());
        register(Long.class, new LongSerializer());
        register(Float.class, new FloatSerializer());
        register(Double.class, new DoubleSerializer());
        register(String.class, new StringSerializer()); // String counts as "nearly" primitive
        //Java Primitive Arrays
        register(boolean[].class, new BooleanArraySerializer());
        register(byte[].class, new ByteArraySerializer());
        register(short[].class, new ShortArraySerializer());
        register(char[].class, new CharacterArraySerializer());
        register(int[].class, new IntegerArraySerializer());
        register(long[].class, new LongArraySerializer());
        register(float[].class, new FloatArraySerializer());
        register(double[].class, new DoubleArraySerializer());
        //Java mainly used Collections
        register(ArrayList.class);
    }

    public <T> void register(Class<T> type) {
        types.define(type);
        serializers.put(type, new ObjectSerializer<>(type));
    }

    public <T> void register(Class<T> type, ISerializer<T> serializer) {
        types.define(type);
        serializers.put(type, serializer);
    }

    /**
     * Redefines the ISerializer for a defined type
     * @param type
     * @param serializer
     * @param <T>
     */
    public <T> void override(Class<T> type, ISerializer<T> serializer) {
        if(types.isDefined(type))
            serializers.put(type, serializer);
    }

    public <T> void unregister(Class<T> type) {
        if(type == NullValue.class)
            throw new TypeDefinitionException("NullValue type CANNOT be undefined.");
        types.undefine(type);
        serializers.remove(type);
    }

    public void unregister(long id) {
        Class type = types.resolve(id);
        serializers.remove(type);
    }

    public <T> int sizeof(T instance) {
        return 8 + sizeofRaw(instance);
    }

    @SuppressWarnings("unchecked")
    public <T> int sizeofRaw(T instance) {
        if(instance == null)
            return sizeofRaw(NULL);
        ISerializer serializer = serializers.get(instance.getClass());
        return serializer.sizeof(this, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> void serialize(Buffer buffer, T object) {
        if(object == null) {
            serialize(buffer, NULL); // override serialization order to NullValue standard
            return;
        }

        Class type = object.getClass();
        long id = types.typeid(type); // throws an error if there is no TypeDefinition
        ISerializer<T> serializer = (ISerializer<T>) serializers.get(type);
        buffer.write(id);
        serializer.serialize(this, buffer, object);
    }

    public Object deserialize(Buffer buffer) {
        long typeid = buffer.readLong();
        Class type = types.resolve(typeid); // throws an error if there is no TypeDefinition
        ISerializer deserializer = serializers.get(type);
        return deserializer.deserialize(this, buffer);
    }
}