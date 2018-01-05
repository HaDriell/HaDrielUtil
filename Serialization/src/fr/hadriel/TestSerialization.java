package fr.hadriel;

import fr.hadriel.serialization.Serialization;
import fr.hadriel.serialization.serializers.*;
import fr.hadriel.serialization.struct.*;
import fr.hadriel.util.Assert;
import fr.hadriel.util.Buffer;
import fr.hadriel.util.typedef.TypeDefinitionException;

/**
 *
 * @author glathuiliere
 */
public class TestSerialization {

    private static final class Message {
        public String c;
        int b;
        private byte a;
        final Integer NULL = null;
        final Float PI = 3.14F;

        public String toString() {
            return String.format("Message a:%d b:%d c:%s", a, b, c);
        }

        private Message() {}
        public Message(byte a, int b, String c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public boolean equals(Object obj) {
            if(obj == this) return true;
            if(obj instanceof Message) {
                Message msg = (Message) obj;
                return a == msg.a && b == msg.b == c.equals(msg.c);
            }
            return false;
        }
    }

    public static void main(String[] args) {
        testAuto();
        testStruct();
    }

    public static void testAuto() {
        Serialization serialization = new Serialization();
        serialization.register(Integer[].class, new ArraySerializer<>(Integer.class));

        Buffer buffer = new Buffer(128); // sounds like a decent Size for buffering single elements
        //Assert Primitives are serializable
        assertSerializable(serialization, buffer, (byte) 200);
        assertSerializable(serialization, buffer, (short) 20_000);
        assertSerializable(serialization, buffer, 'c');
        assertSerializable(serialization, buffer, 200);
        assertSerializable(serialization, buffer, 1_000_000_000_000L);
        assertSerializable(serialization, buffer, 1.5F);
        assertSerializable(serialization, buffer, 0.5);
        assertSerializable(serialization, buffer, false);
        assertSerializable(serialization, buffer, "HelloWorld");

        //Arrays are not support by assertSerializable
        int[] array = new int[]{1, 2, 3};
        buffer.clear();
        serialization.serialize(buffer, array);
        buffer.flip();
        Object deserialization = serialization.deserialize(buffer);
        buffer.clear();
        int[] darray = (int[]) deserialization;
        Assert.assertEqual(array.length, darray.length, "Serialization/Deserialization failed for Arrays");
        for(int i = 0; i < darray.length; i++)
            Assert.assertEqual(array[i], darray[i], " Serialization/Deserialization failed for Arrays");
        //Arrays serialization example test

        //ObjectSerializer Test
        Assert.expect(TypeDefinitionException.class, () -> serialization.serialize(buffer, new Message()));
        serialization.register(Message.class, new ObjectSerializer<>(Message.class));
        assertSerializable(serialization, buffer, new Message((byte) 1, 16, "Helloworld"));
    }

    private static <T> void assertSerializable(Serialization serialization, Buffer buffer, T object) {
        buffer.clear();
        serialization.serialize(buffer, object);
        buffer.flip();
        Object deserialization = serialization.deserialize(buffer);
        buffer.clear();
        Assert.assertEqual(object, deserialization, " Serialization/Deserialization failed");
    }

    public static void testStruct() {
        StPrimitive object;
        StPrimitive deserialization;
        byte[] buffer;

        //StBoolean
        object = new StBoolean(true);
        buffer = new byte[object.getSize()];
        object.serialize(buffer, 0);
        deserialization = Struct.deserialize(buffer, 0);
        if(!deserialization.equals(object)) throw new RuntimeException("Serialization Failed");

        //StBoolean
        object = new StByte((byte) 1);
        buffer = new byte[object.getSize()];
        object.serialize(buffer, 0);
        deserialization = Struct.deserialize(buffer, 0);
        if(!deserialization.equals(object)) throw new RuntimeException("Serialization Failed");

        //StShort
        object = new StShort((short) 1024);
        buffer = new byte[object.getSize()];
        object.serialize(buffer, 0);
        deserialization = Struct.deserialize(buffer, 0);
        if(!deserialization.equals(object)) throw new RuntimeException("Serialization Failed");

        //StInt
        object = new StInt(32000);
        buffer = new byte[object.getSize()];
        object.serialize(buffer, 0);
        deserialization = Struct.deserialize(buffer, 0);
        if(!deserialization.equals(object)) throw new RuntimeException("Serialization Failed");

        //StLong
        object = new StLong(128_000_000_000_000L);
        buffer = new byte[object.getSize()];
        object.serialize(buffer, 0);
        deserialization = Struct.deserialize(buffer, 0);
        if(!deserialization.equals(object)) throw new RuntimeException("Serialization Failed");

        //StFloat
        object = new StFloat(0.5f);
        buffer = new byte[object.getSize()];
        object.serialize(buffer, 0);
        deserialization = Struct.deserialize(buffer, 0);
        if(!deserialization.equals(object)) throw new RuntimeException("Serialization Failed");

        //StDouble
        object = new StDouble(128_000_000_000_000_000D);
        buffer = new byte[object.getSize()];
        object.serialize(buffer, 0);
        deserialization = Struct.deserialize(buffer, 0);
        if(!deserialization.equals(object)) throw new RuntimeException("Serialization Failed");

        //StString
        object = new StString("HelloWorld!");
        buffer = new byte[object.getSize()];
        object.serialize(buffer, 0);
        deserialization = Struct.deserialize(buffer, 0);
        if(!deserialization.equals(object)) throw new RuntimeException("Serialization Failed");

        //StArray
        StArray array = new StArray();
        array.add("Hello");
        array.add("World");
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(true);
        object = array;
        buffer = new byte[object.getSize()];
        object.serialize(buffer, 0);
        deserialization = Struct.deserialize(buffer, 0);
        if(!deserialization.equals(object)) throw new RuntimeException("Serialization Failed");

        //StObject
        StObject database = new StObject();
        database.put("String", "Helloworld");
        database.put("int", 1);
        database.put("long", 2L);
        database.put("boolean", false);
        database.put("array", array);
        object = database;
        buffer = new byte[object.getSize()];
        object.serialize(buffer, 0);
        deserialization = Struct.deserialize(buffer, 0);
        if(!deserialization.equals(object)) throw new RuntimeException("Serialization Failed");
    }
}
