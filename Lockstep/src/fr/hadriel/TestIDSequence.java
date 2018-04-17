package fr.hadriel;

public class TestIDSequence {

    public static void main(String[] args) {
        long id = Long.MAX_VALUE;
        System.out.println(id);
        System.out.println(id & Long.MAX_VALUE);
        id++;
        System.out.println(id);
        System.out.println(id & Long.MAX_VALUE);
    }
}