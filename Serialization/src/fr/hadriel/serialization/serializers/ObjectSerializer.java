package fr.hadriel.serialization.serializers;

import fr.hadriel.serialization.ISerializer;
import fr.hadriel.serialization.Serialization;
import fr.hadriel.util.Buffer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Automic serializer that configures itself on instanciation for a given Class signatur
 * Only takes in account
 * @author glathuiliere
 */
public class ObjectSerializer<T> implements ISerializer<T> {

    private final Class<T> type;
    private final Constructor<T> constructor;
    private final Field[] fields;

    public ObjectSerializer(Class<T> type) {
        this.type = type;

        //fetching public constructor
        try {
            this.constructor = type.getDeclaredConstructor();
        } catch (Exception e) {
            throw new RuntimeException("ObjectSerializer requires an empty constructor", e);
        }

        try {
            List<Field> fieldList = Arrays.stream(type.getDeclaredFields())
                    .filter(f -> !Modifier.isTransient(f.getModifiers()))
                    .filter(f -> !Modifier.isStatic(f.getModifiers()))
                    .sorted(Comparator.comparing(Field::getName))
                    .collect(Collectors.toList());
            //converting back to array (why not list while we're at it ?)
            this.fields = new Field[fieldList.size()];
            fieldList.toArray(fields);

            if(fields.length > 256)
                throw new UnsupportedOperationException("Object has too much fields.");
        } catch (Exception e) {
            throw new RuntimeException("Error while mapping fields ", e);
        }
    }

    public T deserialize(Serialization serialization, Buffer buffer) {
        try {
            //Instanciation before deserialization
            T instance = NEW_INSTANCE(constructor);

            int fieldCount = buffer.readByte() & 0xFF; // read unsigned byte
            while (fieldCount > 0) {
                int index = buffer.readByte() & 0xFF; // read unsigned byte
                Object object = serialization.deserialize(buffer); // deserialize member
//                System.out.println("Field " + index + " (" + fields[index].getName() + "): " + (object == null ? "null" : object.getClass().getName()));
                SET(fields[index], instance, object);
                fieldCount--;
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Error while deserializing " + type.getName(), e);
        }
    }

    public void serialize(Serialization serialization, Buffer buffer, T object) {
        /*
        * AutoSerialization stores fields by index (sorted by name).
        * Null values are not serialized (saving space)
        * once serialized, the object layout is : [fieldCount] [fieldIndex][object] [fieldIndex][object]...
        * */
        try {
            byte count = 0;
            int countPosition = buffer.position();
            buffer.position(countPosition + 1);
            for(int i = 0; i < fields.length; i++) {
                Object value = GET(fields[i], object);
                if(value != null) {
                    count++;
                    buffer.write((byte) i); // index (byte)
                    serialization.serialize(buffer, value); // type (long) + data (variable)
                }
            }
            int endPosisition = buffer.position();
            buffer.position(countPosition); // move back to initial index
            buffer.write(count); // write the serialized Field count
            buffer.position(endPosisition); // move forward to the last position
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object", e);
        }
    }

    public int sizeof(Serialization serialization, T instance) {
        int size = 1; // fields.length
        try {
            for (int i = 0; i < fields.length; i++) {
                Object value = GET(fields[i], instance);
                if(value != null) {
                    size += 1; // field index
                    size += serialization.sizeof(value); // field serialization
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get size of object", e);
        }
        return size;
    }

    /* FIELD Quick accessors*/

    private static <T> T NEW_INSTANCE(Constructor<T> constructor) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if(!constructor.isAccessible()) constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private static void SET(Field field, Object owner, Object value) throws IllegalAccessException {
        if(!field.isAccessible()) field.setAccessible(true);
        field.set(owner, value);
    }

    private static Object GET(Field field, Object owner) throws IllegalAccessException {
        if(!field.isAccessible()) field.setAccessible(true);
        return field.get(owner);
    }
}
