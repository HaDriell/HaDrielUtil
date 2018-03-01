package fr.hadriel.core.media;

import fr.hadriel.util.Timer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.lwjgl.glfw.GLFW.*;

public final class GFX {
    private GFX() {}

    // GFX Configuration options //
    private static float minFrameTime;

    public static void setTargetFPS(float fps) {
        if(fps <= 0)
            minFrameTime = 0f; // no limit value
        else
            minFrameTime = 1f / fps;
    }

    // GFX Runtime executor //

    private final static List<MediaTask> tasks = new LinkedList<>();
    private final static Lock lock = new ReentrantLock();
    private static ExecutorService executor;

    private static void run() {
        if(!glfwInit())
            return;

        Timer timer = new Timer();
        do {
            float dt = timer.elapsed();

            lock.lock();
            timer.reset();
            tasks.removeIf(t -> t.execute(dt)); // execute tasks and removes them if they SHOULD be removed.
            lock.unlock();

            if(timer.elapsed() < minFrameTime) {
                try { Thread.sleep((int) (minFrameTime - timer.elapsed()) ); } catch (InterruptedException ignore) {}
            }

        } while (!terminating());
        glfwTerminate();
    }

    private static synchronized boolean terminating() {
        if(tasks.isEmpty()) {
            executor.shutdown();
            executor = null;
            return true;
        }
        return false;
    }

    public static synchronized MediaTask add(MediaTask task) {
        if(task == null)
            return null;

        lock.lock();
        tasks.add(task);
        lock.unlock();

        if(executor == null) {
            executor = Executors.newSingleThreadExecutor();
            executor.submit(GFX::run);
        }
        return task;
    }

    public static synchronized void remove(MediaTask task) {
        lock.lock();
        tasks.remove(task);
        lock.unlock();
    }
}