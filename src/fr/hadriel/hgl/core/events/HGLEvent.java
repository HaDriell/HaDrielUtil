package fr.hadriel.hgl.core.events;

import fr.hadriel.event.IEvent;

import java.util.concurrent.CountDownLatch;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public class HGLEvent implements IEvent {

    private CountDownLatch handling = new CountDownLatch(1);

    public void await() throws InterruptedException {
        handling.await();
    }

    public void consume() {
        if(handling.getCount() > 0) handling.countDown();
    }
}