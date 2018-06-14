package fr.hadriel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Test {
    public static List<Integer> objects = new ArrayList<>();
    public static Integer[] objectArray = new Integer[10_000_000];

    public static void main(String[] args) {
        for (int i = 0; i < 10_000_000; i++) {
            new Integer(0);
        }

        for (int i = 0; i < objectArray.length; i++) {
            objects.add(new Integer(0));
            objectArray[i] = new Integer(0);
        }
        System.out.println("Starting test");
        list();
        iterator();
        array();
        liststream();
        arraystream();
    }

    public static void list() {
        long start = System.nanoTime();

        int sum = 0;
        for (int i : objects) {
            sum += i;
        }

        long end = System.nanoTime();
        System.out.println("List time: " + (end - start) / 1000000.0f);
    }

    public static void iterator() {
        long start = System.nanoTime();

        Iterator<Integer> it = objects.iterator();
        int sum = 0;
        while (it.hasNext()) {
            int i = it.next();
            sum += i;
        }

        long end = System.nanoTime();
        System.out.println("Iterator time: " + (end - start) / 1000000.0f);
    }

    public static void array() {
        long start = System.nanoTime();

        int sum = 0;
        for (int i : objectArray) {
            sum += i;
        }

        long end = System.nanoTime();
        System.out.println("Array time: " + (end - start) / 1000000.0f);
    }

    public static void liststream() {
        long start = System.nanoTime();

        int sum = objects.stream().mapToInt(i -> i).sum();

        long end = System.nanoTime();
        System.out.println("Stream List time: " + (end - start) / 1000000.0f);
    }

    public static void arraystream() {
        long start = System.nanoTime();

        int sum = Arrays.stream(objectArray).mapToInt(i -> i).sum();

        long end = System.nanoTime();
        System.out.println("Stream Array time: " + (end - start) / 1000000.0f);
    }
}