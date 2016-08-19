package fr.hadriel;

import fr.hadriel.serialization.json.JsArray;
import fr.hadriel.serialization.json.JsObject;
import fr.hadriel.serialization.struct.*;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class TestSerial {

    public static void main(String[] args) {
        System.out.println("STRUCT_TEST");
        struct();
        System.out.println("JSON_TEST");
        json();
    }

    public static void json() {
        JsObject object = new JsObject();
        JsArray list = new JsArray();
        list.add("Helloworld");
        list.add(66);
        list.add(null);

        object.put("list", list);
        object.put("sinus", "sinus");
        object.put("pi", 3.14f);

        System.out.println(object.toString());
    }

    public static void struct() {
        StObject object = new StObject();
        StArray list = new StArray();
        list.add(new StString("Helloworld"));
        list.add(new StInt(66));

        object.put("list", list);
        object.put("sinus", "sinus");
        object.put("pi", 3.14f);

        byte[] buffer = new byte[object.getSize()];
        object.serialize(buffer, 0);
        object = (StObject) Struct.deserialize(buffer, 0);
        System.out.println(object);
    }
}