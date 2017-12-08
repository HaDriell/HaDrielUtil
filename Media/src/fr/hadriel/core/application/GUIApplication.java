package fr.hadriel.core.application;

import fr.hadriel.application.IHDUApplication;
import fr.hadriel.graphics.g2d.G2DWindow;
import fr.hadriel.graphics.glfw3.GLFWwindow;
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

    public static void runLater(Runnable runnable) {
        runLater((dt) -> {
            runnable.run();
            return true;
        });
    }

    public static void runLater(GUITask task) {
        application.submit(task); // TODO : null application error handling
    }

    public static void runAndWait(GUITask task) {
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
        if(application != null)
            throw new RuntimeException("Unable create multiple GUIApplications at the same time !");
        application = this;

        //TODO : create a first window and pass it to the GUIApplication ?
        G2DWindow window = new G2DWindow();

        new Thread(() -> start(window), "Application Thread").start(); // real run function
        Timer timer = new Timer();

        System.out.println("Tasks:" + tasks.size());
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

    protected abstract void start(G2DWindow window); // TODO : add a Window in param
}
