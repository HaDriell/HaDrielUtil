package fr.hadriel.math.geometry;

public strictfp final class Epsilon {
    public static final float E = compute();

    public static float compute() {
        float e = 0.5f;
        while (1f + e > 1f) {
            e *= 0.5;
        }
        return e;
    }
}
