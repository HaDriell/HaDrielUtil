package fr.hadriel.threading;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public abstract class Loop {

    private Thread thread;
    private boolean running;

    public boolean isRunning() {
        return running;
    }

    public void requireRunning() {
        if(!running)
            throw new IllegalStateException(this + " must be running to call this method!");
    }

    public synchronized void start() {
        if(running)
            return;
        running = true;
        onStart();
        thread = new Thread(this::run);
        thread.start();
    }

    public synchronized void stop() throws InterruptedException {
        if(!running)
            return;
        running = false;
        thread.join();
        thread = null;
        onStop();
    }

    public synchronized void interrupt() {
        if(!running)
            return;
        running = false;
        thread.interrupt();
        thread = null;
        onStop();
    }

    private void run() {
        while (running)
            onLoop();
    }

    protected abstract void onStart();
    protected abstract void onLoop();
    protected abstract void onStop();
}
