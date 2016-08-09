package fr.hadriel;

import fr.hadriel.serialization.StEntry;
import fr.hadriel.serialization.StFloat;
import fr.hadriel.serialization.StList;
import fr.hadriel.serialization.StString;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class TestSerial {

    public static void main(String[] args) {
        StList list = new StList();
        list.add(new StString("Helloworld"));
        list.add(new StFloat(3.14f));

        byte[] buffer = new byte[list.getSize()];
        list.serialize(buffer, 0);
        list = (StList) StEntry.deserialize(buffer, 0);
        for(StEntry entry : list) {
            System.out.println(entry);
        }
    }
}
