package fr.hadriel.graphics.core;

import fr.hadriel.threading.Loop;
import fr.hadriel.util.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
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

    public static void wakeup() {
        if (!instance.isRunning()) instance.start();
    }

    public static void runAndWait(Task... tasks) {
        final CountDownLatch cd = new CountDownLatch(1);
        instance.submitTasks(() -> {
            for(Task task : tasks)
                task.execute();
            cd.countDown();
        });
        try { cd.await(); } catch (InterruptedException ignore) {}
    }

    public static void runLater(Task... tasks) {
        instance.submitTasks(tasks);
        wakeup();
    }

    public static void register(Hook hook) {
        runLater(() -> instance.addHook(hook)); //Tasks resolve before the Hooks are called
        wakeup(); //make sure the Thread is running again
    }

    public static void unregister(Hook hook) {
        runLater(() -> instance.removeHook(hook)); //Tasks resolve before the Hooks are called
        wakeup(); //make sure the Thread is running again
    }

    public static boolean isRegistered(Hook hook) {
        return instance.hasHook(hook);
    }


    // Singleton Definition

    private List<Task> tasks;
    private Lock tasksLock;

    private List<Hook> hooks;
    private Lock hooksLock;

    private GraphicsThread() {
        this.hooks = new ArrayList<>();
        this.tasks = new ArrayList<>(1024); // we want a large default Buffer
        this.hooksLock = new ReentrantLock();
        this.tasksLock = new ReentrantLock();
    }

    private void submitTasks(Task... submits) {
        tasksLock.lock();
        tasks.addAll(Arrays.asList(submits));
        tasksLock.unlock();
    }

    private boolean hasHook(Hook hook) {
        hooksLock.lock();
        boolean result = hooks.contains(hook);
        hooksLock.unlock();
        return result;
    }

    private void addHook(Hook hook) {
        if(hooks.add(hook))
            hook.initialize();
    }

    private void removeHook(Hook hook) {
        if(hooks.remove(hook))
            hook.terminate();
    }

    protected void onStart() {
        if (!glfwInit())
            throw new RuntimeException("Unable to initialize GLFW");
        glfwSwapInterval(GLFW_TRUE);
        hooksLock.lock();
        hooks.forEach(Hook::initialize);
        hooksLock.unlock();
    }

    protected void onLoop() {
        Timer timer = new Timer();
        timer.reset();

        //Execute Tasks
        tasksLock.lock();
        tasks.forEach(Task::execute);
        tasks.clear();
        tasksLock.unlock();

        //Execute Hooks & Check if Idle
        hooksLock.lock();
        hooks.forEach(Hook::update);
        setRunning(isIdle());
        hooksLock.unlock();

        //Try to avoid CPU burning in case of lightweight Hooks & Tasks
        if(timer.elapsed() * 1000f < 1)
            try { Thread.sleep(1); } catch (InterruptedException ignore) {}
    }

    private boolean isIdle() {
        for(Hook hook : hooks) {
            if(!hook.isIdle())
                return true;
        }
        return false;
    }

    protected void onStop() {
        hooksLock.lock();
        hooks.forEach(Hook::terminate);
        hooksLock.unlock();
    }
}