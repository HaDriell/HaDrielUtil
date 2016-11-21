package fr.hadriel;


import fr.hadriel.util.PRNG;

/**
 * Created by HaDriel on 21/10/2016.
 */
public class TestPRNG {
    public static void main(String[] args) {
        PRNG p = new PRNG();
        while (true) {
            double d = p.nextDouble();
            if(d < 0 || d > 1) System.out.println(d);
        }
    }
}