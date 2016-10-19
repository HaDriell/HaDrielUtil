package fr.hadriel;


import fr.hadriel.serialization.json.*;

import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Created by glathuiliere on 18/10/2016.
 */
public class TestJson {
    public static void main(String[] unused) throws Exception {
        String json = new String(Files.readAllBytes(Paths.get("json.json")));
        String model = json.replace('\n', ' ').replace('\r', ' ').replace(" ", "");
        JsPrimitive js = Json.deserialize(json);
        System.out.println(model);
        System.out.println(js);
    }
}