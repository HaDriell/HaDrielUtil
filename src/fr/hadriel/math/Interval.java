package fr.hadriel.math;

/**
 * Created by glathuiliere on 05/09/2016.
 */
public class Interval {

    public float min;
    public float max;

    public Interval(float a, float b) {
        if(a < b) {
            this.min = a;
            this.max = b;
        } else {
            this.min = b;
            this.max = a;
        }
    }

    public float length() {
        return max - min;
    }

    public boolean overlaps(Interval b) {
        return getOverlapValue(this, b) > 0;
    }

    public static float getOverlapValue(Interval a, Interval b) {
        float overlap = Math.min(a.max, b.max) - Math.max(a.min, b.min);
        return overlap > 0 ? overlap : 0;
    }

    public String toString() {
        return String.format("Interval(%f, %f)", min, max);
    }
}