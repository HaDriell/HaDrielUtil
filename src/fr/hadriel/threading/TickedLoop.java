package fr.hadriel.threading;

import fr.hadriel.util.Timer;

/**
 * Created by glathuiliere setOn 08/08/2016.
 */
public abstract class TickedLoop extends Loop {

    private Timer timer;
    private float sleepTime;

    protected TickedLoop() {
        this.timer = new Timer();
        setTickRate(60);
    }

    public void setTickRate(int rate) {
        sleepTime = 1F / rate;
    }

    public int getTickRate() {
        return (int) (1F / sleepTime);
    }

    protected final void onLoop() {
        onTick();
        int sleepDuration = (int) (1000F * sleepTime - timer.elapsed());
        timer.reset();
        if(sleepDuration > 0) {
            try {
                Thread.sleep(sleepDuration);
            } catch (InterruptedException ignore) {}
        }
    }

    protected abstract void onTick();

    public float getDelta() {
        return timer.elapsed();
    }
}
