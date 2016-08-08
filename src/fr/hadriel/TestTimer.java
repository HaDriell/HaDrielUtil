package fr.hadriel;

import fr.hadriel.time.Timer;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class TestTimer {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.reset();
        System.out.println(timer.elapsed());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignore) {}
        System.out.println(timer.elapsed());
    }
}
