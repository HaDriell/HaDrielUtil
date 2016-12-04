package fr.hadriel.hgl.glfw;

import fr.hadriel.event.IEvent;

import java.util.concurrent.CountDownLatch;

/**
 * Created by HaDriel on 03/12/2016.
 */
public class SyncEvent implements IEvent {
    private CountDownLatch latch = new CountDownLatch(1);

    public void await() {
        try {
            latch.await();
        } catch (InterruptedException ignore) {}
    }
}