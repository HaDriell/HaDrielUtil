package fr.hadriel.time;

/**
 * Created by glathuiliere on 08/08/2016.
 *
 * Utility class to measure elapsed time during runtime.
 */
public class Timer {

    private long anchor = System.nanoTime();

    /**
     *
     * @return elapsed seconds since the last reset() or the Timer creation
     */
    public float elapsed() {
        return (System.nanoTime() - anchor) / 1_000_000_000F;
    }

    /**
     * sets the timer anchor to 'now'. elapsed() should return ~0f right after this method.
     */
    public void reset() {
        anchor = System.nanoTime();
    }


    public void set(float elapsedMilliseconds) {
        anchor = (long) (System.nanoTime() - elapsedMilliseconds * 1_000_000F);
    }

    /**
     * sets the timer anchor to the time value, so the elapsed() method will be relative to that anchor.
     * @param time the anchor value.
     */
    public void set(long time) {
        anchor = time;
    }

    /**
     * pushes the timer anchor in the "past"
     * @param time in seconds
     */
    public void add(float time) {
        anchor -= time * 1_000_000_000F;
    }

    /**
     * pushes the timer anchor in the "future"
     * @param time in seconds
     */
    public void remove(float time) {
        anchor += time * 1_000_000_000F;
    }
}