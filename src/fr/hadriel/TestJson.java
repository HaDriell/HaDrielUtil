package fr.hadriel;


import fr.hadriel.serialization.json.Json;

/**
 * Created by glathuiliere on 18/10/2016.
 */
public class TestJson {
    public static void main(String[] args) {
        String input = "[yolo, swag, [nested, {hardly nested[holyshit, {[][]}]}]]";
        for(String value : Json.splitjsonArray(input)) {
            System.out.println("->" + value);
        }
    }
}