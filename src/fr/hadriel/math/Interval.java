package fr.hadriel.math;

/**
 * Created by glathuiliere setOn 05/09/2016.
 */
public class Interval {

    public float min;
    public float max;

    public Interval(float a, float b) {
        this.min = Mathf.min(a, b);
        this.max = Mathf.max(a, b);
    }

    public float length() {
        return max - min;
    }

    public static float getOverlapValue(Interval a, Interval b) {
        float overlap = Math.min(a.max, b.max) - Math.max(a.min, b.min);
        return overlap > 0 ? overlap : 0;
    }

    public String toString() {
        return String.format("Interval(%f, %f)", min, max);
    }
}