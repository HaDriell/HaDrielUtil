package fr.hadriel.math.geometry;

public class Interval {

    public final float min;
    public final float max;

    public Interval(float a, float b) {
        if(a <= b) {
            this.min = a;
            this.max = b;
        } else {
            this.min = b;
            this.max = a;
        }
    }

    public boolean overlaps(Interval interval) {
        return !(this.min > interval.max || interval.min > this.max);
    }

    public float getOverlap(Interval interval) {
        return Math.min(max, interval.max) - Math.max(min, interval.min);
    }

    public Interval union(Interval interval) {
        return new Interval(Math.min(min, interval.min), Math.max(max, interval.max));
    }

    public Interval intersection(Interval interval) {
        if(!overlaps(interval)) return new Interval(0, 0);
        return new Interval(Math.max(min, interval.min), Math.min(max, interval.max));
    }
}