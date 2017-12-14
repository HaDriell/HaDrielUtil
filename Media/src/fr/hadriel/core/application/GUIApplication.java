package fr.hadriel.core.application;

import fr.hadriel.application.IHDUApplication;
import fr.hadriel.gui.Window;
import fr.hadriel.util.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public abstract class GUIApplication implements IHDUApplication {

    private static GUIApplication application = null;
    private static Thread graphicsThread = null;

    private static void requireGUIApplication() {
        if(application == null) throw new RuntimeException("GUIApplication not initialized !");
    }

    public static boolean isCurrentThreadGraphics() {
        return Thread.currentThread() == graphicsThread;
    }

    public static void runLater(Runnable runnable) {
        runLater((dt) -> {
            runnable.run();
            return true;
        });
    }

    public static void runLater(GUITask task) {
        requireGUIApplication();
        application.submit(task);
    }

    public static void runAndWait(GUITask task) {
        requireGUIApplication();
        CountDownLatch cd = new CountDownLatch(1);
        application.submit((dt) -> {
            if(task.execute(dt)) {
                cd.countDown();
                return true;
            }
            return false;
        });
        try { cd.await(); } catch (InterruptedException ignore) {}
    }


    private final List<GUITask> tasks;
    private final Lock lock;

    protected GUIApplication(GUITask... tasks) {
        this.tasks = new ArrayList<>(Arrays.asList(tasks));
        this.lock = new ReentrantLock();
    }

    public final void run(String[] args) {
        if(graphicsThread != null && graphicsThread != Thread.currentThread())
            throw new IllegalStateException("Trying to set Application while not Graphics Thread");
        if(application != null)
            throw new RuntimeException("Unable create multiple GUIApplications at the same time !");

        graphicsThread = Thread.currentThread();
        application = this;


        final Window window = new Window();
        //We must delay the Application start for the next update because of the Window initialization step
        runLater(() -> {
            new Thread(() -> start(window), "Application Thread").start(); // real run function
        });

        Timer timer = new Timer();
        do {
            float dt = timer.elapsed();
            timer.reset();
            lock.lock();
            tasks.removeIf(task -> task.execute(dt));
            lock.unlock();

        } while (!tasks.isEmpty());
    }

    public void submit(GUITask task) {
        lock.lock();
        tasks.add(task);
        lock.unlock();
    }

    protected abstract void start(Window window); // TODO : add a Window in param
}
