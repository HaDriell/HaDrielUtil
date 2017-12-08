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

    protected void setRunning(boolean running) {
        this.running = running;
    }

    public void requireRunning() {
        if(!running)
            throw new IllegalStateException(this + " must be running to call this method!");
    }


    public synchronized void start() {
        if(running)
            return;
        running = true;
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
    }

    private void run() {
        onStart();
        while (running)
            onLoop();
        onStop();
    }

    /**
     * this method is called when the media started successfully
     */
    protected abstract void onStart();

    /**
     * this method is called indefinitely while the Loop is running
     */
    protected abstract void onLoop();

    /**
     * this method is called only when Thread terminates by angle normal stop(); (not when interrupted !)
     */
    protected abstract void onStop();
}