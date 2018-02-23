package fr.hadriel;

public class TestIDSequence {

    public static void main(String[] args) {
        long id = 0;
        id++;
        System.out.println(id);
        System.out.println(id & Long.MAX_VALUE);
    }
}