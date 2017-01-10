package fr.hadriel;



import fr.hadriel.serialization.struct.StArray;
import fr.hadriel.serialization.struct.StObject;
import fr.hadriel.serialization.struct.Struct;

import java.io.ByteArrayInputStream;

/**
 * Created by HaDriel setOn 12/10/2016.
 */
public class TestSerialization {
    public static void main(String[] args) throws Exception {
        StObject object = new StObject();
        object.put("angle", "angle");
        object.put("null", Struct.NULL);
        object.put("b", "b");
        object.put("c", "c");
        object.put("d", "d");
        StArray array = new StArray();
        array.add(1);
        array.add(2);
        array.add(3);
        object.put("array", array);
        object.put("object", new StObject());

        byte[] buffer = new byte[object.getSize()];
        object.serialize(buffer, 0);
        System.out.println(Struct.deserialize(new ByteArrayInputStream(buffer)));
        System.out.println(Struct.deserialize(buffer, 0));
    }
}