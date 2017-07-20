package fr.hadriel.main.lwjgl;

import fr.hadriel.main.threading.Loop;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import static org.lwjgl.glfw.GLFW.*;
/**
 * Created by gauti on 19/07/2017.
 *
 * New graphics platform handler.
 * Is defined as a JavaFX Platform general purpose-like thing.
 */

public class GraphicsThread extends Loop {
    private static final GraphicsThread instance = new GraphicsThread();

    public static void submit(Runnable task) {
        instance.start();
        instance.pushTask(task);
    }

    public static void register(Runnable hook) {
        instance.start();
        instance.addHook(hook);
    }

    public static void unregister(Runnable hook) {
        instance.removeHook(hook);
    }

    public static boolean isRegistered(Runnable hook) {
        return instance.hasHook(hook);
    }

    private Queue<Runnable> tasks;
    private Lock tasksLock;

    private List<Runnable> hooks;
    private Lock hooksLock;

    private GraphicsThread() {
        this.hooks = new ArrayList<>();
        this.tasks = new LinkedList<>();
        this.hooksLock = new ReentrantLock();
        this.tasksLock = new ReentrantLock();
    }

    public void pushTask(Runnable runnable) {
        tasksLock.lock();
        tasks.offer(runnable); // avoid Exception throwing
        tasksLock.unlock();
    }

    public boolean hasHook(Runnable runnable) {
        hooksLock.lock();
        boolean result = hooks.contains(runnable);
        hooksLock.unlock();
        return result;
    }

    public void addHook(Runnable runnable) {
        tasksLock.lock();
        tasks.add(() -> hooks.add(runnable));
        tasksLock.unlock();
    }

    public void removeHook(Runnable runnable) {
        tasksLock.lock();
        tasks.add(() -> hooks.remove(runnable));
        tasksLock.unlock();
    }

    protected void onStart() {
        if(!glfwInit())
            throw new RuntimeException("Unable to initialize GLFW");
        glfwSwapInterval(GLFW_TRUE);
    }

    protected void onLoop() {
        tasksLock.lock();
        hooksLock.lock();

        //Run & Clear Tasks
        tasks.forEach(Runnable::run);
        tasks.clear();

        //Run Hooks
        hooks.forEach(Runnable::run);

        //Close on next step
        if(hooks.isEmpty() && tasks.isEmpty()) setRunning(false);
        tasksLock.unlock();
        hooksLock.unlock();
    }

    protected void onStop() {
        glfwTerminate();
    }
}