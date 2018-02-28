package fr.hadriel.core.media;

import fr.hadriel.util.Timer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author glathuiliere
 */
public final class Medias {
    private Medias() {}

    private static final List<MediaTask> actions = new LinkedList<>();
    private static final Lock lock = new ReentrantLock();
    private static Thread thread;

    private static synchronized void wakeup() {
        if(thread != null)
            return;
        thread = new Thread(Medias::run, "Medias.Worker");
        thread.setDaemon(true);
        thread.start();
    }

    public static void execute(Runnable task) {
        execute(new MediaTask() {
            public void onBegin() { }

            public boolean onExecute(float dt) {
                task.run();
                return true;
            }

            public void onEnd() { }
        });
    }

    public static void execute(MediaTask action) {
        lock.lock();
        actions.add(action);
        lock.unlock();
        wakeup();
    }

    public static void interrupt(MediaTask action) {
        lock.lock();
        actions.remove(action);
        lock.unlock();
    }

    private static void run() {
        Timer timer = new Timer();
        while (actions.size() > 0) {
            //get delta time
            float dt = timer.elapsed();
            timer.reset();

            //update all actions (and remove them if needed)
            lock.lock();
            actions.removeIf(action -> action.execute(dt));
            lock.unlock();

            //avoid burning too much CPU on lightweight work (less than 1ms looping)
            if(timer.elapsed() < 0.0001f) {
                try { Thread.sleep(1); } catch (InterruptedException ignore) {}
            }
        }
        //remove the reference from the media so that wakeup() can insanciate another one
        Medias.thread = null;
    }

}