package fr.hadriel;

import fr.hadriel.serialization.struct.*;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class TestSerial {

    public static void main(String[] args) {
        StObject object = new StObject();
        StList list = new StList();
        list.add(new StString("Helloworld"));
        list.add(new StInt(66));

        object.put("list", list);
        object.put("sinus", "sinus");
        object.put("pi", 3.14f);

        byte[] buffer = new byte[object.getSize()];
        object.serialize(buffer, 0);
        object = (StObject) StructEntry.deserialize(buffer, 0);
        System.out.println(object);
    }
}